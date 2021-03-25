package nikita488.zycraft.multiblock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.multiblock.tank.MultiFluidTank;
import nikita488.zycraft.multiblock.tile.MultiChildTile;
import nikita488.zycraft.multiblock.tile.ValveTile;
import nikita488.zycraft.network.ZYChannel;
import nikita488.zycraft.tile.ZYTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger NEXT_MULTI_ID = new AtomicInteger();

    protected final MultiType<?> type;
    protected final World world;
    protected final ChunkPos mainChunk;
    protected final MultiManager manager;
    protected final ObjectList<BlockPos> childBlocks = new ObjectArrayList<>();
    protected final ObjectSet<ChunkPos> parentChunks = new ObjectOpenHashSet<>();

    private int id = NEXT_MULTI_ID.getAndIncrement();
    protected boolean valid;
    protected boolean needsUpdate;

    public MultiBlock(MultiType<?> type, World world, ChunkPos mainChunk)
    {
        this.type = type;
        this.world = world;
        this.mainChunk = mainChunk;
        this.manager = MultiManager.getInstance(world);
    }

    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    public void tick() {}

    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int lightMap, float partialTicks) {}

    public AxisAlignedBB getRenderBoundingBox()
    {
        return getBoundingBox();
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        return true;
    }

    public ActionResultType onChildInteraction(MultiChildTile child, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
    }

    public int getChildLightValue(MultiChildTile child)
    {
        return 0;
    }

    @Nullable
    public static MultiBlock create(World world, ChunkPos pos, CompoundNBT tag)
    {
        String name = tag.getString("Name");
        return Optional.ofNullable(ZYRegistries.MULTI_TYPES.get().getValue(new ResourceLocation(name))).map(type ->
        {
            try
            {
                return type.create(world, pos);
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Failed to create MultiBlock {}", name, throwable);
                return null;
            }
        }).map(multiBlock ->
        {
            try
            {
                multiBlock.load(tag);
                multiBlock.initChildBlocks();
                return multiBlock;
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Failed to load data for MultiBlock {}", name, throwable);
                return null;
            }
        }).orElseGet(() ->
        {
            LOGGER.warn("Skipping MultiBlock {}", name);
            return null;
        });
    }

    public void addChildBlocks(Cuboid6i cuboid)
    {
        addChildBlocks(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
    }

    public void addChildBlocks(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                    addChildBlock(new BlockPos(x, y, z));
    }

    public void addChildBlock(BlockPos pos)
    {
        childBlocks.add(pos);
        parentChunks.add(new ChunkPos(pos));
    }

    public void onChildValidation(MultiChildTile child) {}
    public void onChildInvalidation(MultiChildTile child) {}

    @Nullable
    public MultiChildTile getMultiChild(BlockPos pos)
    {
        if (!world.isBlockPresent(pos))
        {
            ZYCraft.LOGGER.error("THA BLOCK AT {} IS NOT LOADED", pos);
            return null;
        }

        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof MultiChildTile ? (MultiChildTile)tile : null;
    }

    public void validate()
    {
        if (childBlocks.isEmpty())
            return;

        for (BlockPos pos : childBlocks)
            if (getMultiChild(pos) == null && (world.isRemote || !MultiChildType.tryConvertBlock(world, pos)))
                return;

        for (BlockPos pos : childBlocks)
        {
            MultiChildTile child = getMultiChild(pos);
            if (child == null)
                continue;
            child.onMultiValidation(this);
            onChildValidation(child);
        }

        this.valid = true;
    }

    public void invalidate(boolean destroy)
    {
        if (!world.isRemote && destroy)
        {
            SetMultimap<LazyOptional<IFluidHandler>, ValveTile> capabilities = HashMultimap.create();

            for (BlockPos pos : childBlocks)
            {
                ValveTile valve = ZYTiles.VALVE.getNullable(world, pos);

                if (valve == null)
                    continue;

                LazyOptional<IFluidHandler> capability = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, valve);//we need full tank here, not just level

                if (capability.isPresent())
                    capabilities.put(capability, valve);
            }

            for (LazyOptional<IFluidHandler> capability : capabilities.keySet())
            {
                IFluidHandler handler = capability.orElse(EmptyFluidHandler.INSTANCE);

                if (handler instanceof MultiFluidTank.Level)
                    handler = ((MultiFluidTank.Level)handler).getTank();

                if (handler.getFluidInTank(0).isEmpty())
                    continue;

                ObjectList<ValveTile> valves = new ObjectArrayList<>();

                for (ValveTile valve : capabilities.get(capability))
                    if (valve.getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, -1) == capability && valve.getStoredFluid().isEmpty())
                        valves.add(valve);

                if (valves.isEmpty())
                    continue;

                FluidStack stack = handler.getFluidInTank(0).copy();

                stack.setAmount(stack.getAmount() / valves.size());

                for (ValveTile valve : valves)
                {
                    valve.setStoredFluid(stack);
                    valve.markDirty();
                }
            }
        }

        this.valid = false;

        //TODO: Check for dead lock
        for (BlockPos pos : childBlocks)
        {
            MultiChildTile child = getMultiChild(pos);
            if (child == null)
                continue;
            child.onMultiInvalidation(this);
            onChildInvalidation(child);
        }

        updateLight();
    }

    public void create()
    {
        manager.addMultiBlock(this);
    }

    public void destroy()
    {
        manager.removeMultiBlock(this, true);
    }

    public void markDirty()
    {
        if (!world.isRemote && world.chunkExists(mainChunk.x, mainChunk.z))
            world.getChunk(mainChunk.x, mainChunk.z).markDirty();
    }

    public void scheduleUpdate()
    {
        manager.scheduleMultiUpdate(this);
    }

    public abstract void initChildBlocks();

    public void encode(PacketBuffer buffer) {}
    public void decode(PacketBuffer buffer) {}

    public void encodeUpdate(PacketBuffer buffer)
    {
        encode(buffer);
    }

    public void decodeUpdate(PacketBuffer buffer)
    {
        decode(buffer);
    }

    public void save(CompoundNBT tag) {}
    public void load(CompoundNBT tag) {}

    public void updateLight()
    {
        for (BlockPos pos : childBlocks)
            world.getLightManager().checkBlock(pos);
    }

    public <MSG> void sendToTracking(MSG message)
    {
        ZYChannel.INSTANCE.send(ZYChannel.TRACKING_MULTI_BLOCK.with(() -> this), message);
    }

    public boolean isMainChunk(ChunkPos pos)
    {
        return pos.equals(mainChunk);
    }

    public boolean isLoaded()
    {
        for (ChunkPos pos : parentChunks)
            if (!world.getChunkProvider().chunkExists(pos.x, pos.z))
                return false;
        return true;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nonnull MultiChildTile child)
    {
        return LazyOptional.empty();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj == this || (obj instanceof MultiBlock && id == ((MultiBlock)obj).id);
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Nonnull
    public MultiType<?> getType()
    {
        return type;
    }

    @Nonnull
    public World world()
    {
        return world;
    }

    @Nonnull
    public ChunkPos mainChunk()
    {
        return mainChunk;
    }

    public int id()
    {
        return id;
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public ObjectList<BlockPos> childBlocks()
    {
        return childBlocks;
    }
}
