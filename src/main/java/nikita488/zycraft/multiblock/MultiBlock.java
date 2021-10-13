package nikita488.zycraft.multiblock;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.BlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.multiblock.child.tile.ValveTile;
import nikita488.zycraft.network.ZYPackets;
import nikita488.zycraft.util.Cuboid6i;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBlock
{
    private static final AtomicInteger NEXT_MULTI_ID = new AtomicInteger();
    final MultiType<?> type;
    final MultiManager manager;
    final World world;
    final ChunkPos mainChunk;
    final ObjectList<BlockPos> childBlocks = new ObjectArrayList<>();
    final ObjectList<BlockPos> interfaces = new ObjectArrayList<>();
    final ObjectSet<ChunkPos> parentChunks = new ObjectOpenHashSet<>();
    private int id;
    boolean valid;
    private boolean changed;

    public MultiBlock(MultiType<?> type, World world, ChunkPos mainChunk)
    {
        this.type = type;
        this.manager = MultiManager.getInstance(world);
        this.world = world;
        this.mainChunk = mainChunk;
        this.id = !world.isClientSide() ? NEXT_MULTI_ID.incrementAndGet() : 0;
    }

    public final void form()
    {
        manager.addMultiBlock(this, AddingReason.FORMED);
    }

    public final void destroy()
    {
        manager.removeMultiBlock(this, RemovalReason.DESTROYED);
    }

    public final void addChildBlocks(Cuboid6i cuboid)
    {
        addChildBlocks(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
    }

    public final void addChildBlocks(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                    addChildBlock(new BlockPos(x, y, z));
    }

    public final void addChildBlock(BlockPos pos)
    {
        childBlocks.add(pos.immutable());
        parentChunks.add(new ChunkPos(pos));
    }

    public final void addInterface(BlockPos pos)
    {
        interfaces.add(pos.immutable());
    }

    public void validate(AddingReason reason)
    {
        if (childBlocks.isEmpty())
            return;

        this.valid = true;

        for (BlockPos pos : childBlocks)
        {
            if (!world.isLoaded(pos))
                continue;

            TileEntity tile = world.getBlockEntity(pos);

            if (tile instanceof IMultiChild)
            {
                IMultiChild child = (IMultiChild)tile;
                child.onMultiValidation(this);
                onChildValidation(child);
            }
        }
    }

    public void onDestroy()
    {
        splitFluids();
    }

    protected void invalidateCapabilities() {}

    public void invalidate(RemovalReason reason)
    {
        if (!world.isClientSide() && reason.isDestroyed())
            onDestroy();

        this.valid = false;

        for (BlockPos pos : childBlocks)
        {
            if (!world.isLoaded(pos))
                continue;

            TileEntity tile = world.getBlockEntity(pos);

            if (tile instanceof IMultiChild)
            {
                IMultiChild child = (IMultiChild)tile;
                child.onMultiInvalidation(this);
                onChildInvalidation(child);
            }
        }

        invalidateCapabilities();
    }

    public void onChildValidation(IMultiChild child) {}
    public void onChildInvalidation(IMultiChild child) {}

    public abstract void initChildBlocks();

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
    }

    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return state.getLightEmission();
    }

    public final void sendMultiUpdated()
    {
        manager.sendMultiUpdated(this);
    }

    protected final void splitFluids()
    {
        Object2ObjectMap<IFluidHandler, ObjectList<ValveTile>> tankValves = new Object2ObjectArrayMap<>();

        for (BlockPos pos : interfaces)
        {
            if (!world.isLoaded(pos))
                continue;

            LazyOptional<IFluidHandler> capability = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, pos);
            IFluidHandler tank = capability.orElse(EmptyFluidHandler.INSTANCE);

            if (!capability.isPresent() || tank.getFluidInTank(0).isEmpty())
                continue;

            TileEntity tile = world.getBlockEntity(pos);

            if (tile instanceof ValveTile)
            {
                ValveTile valve = (ValveTile)tile;

                if (!valve.hasStoredFluid())
                    tankValves.computeIfAbsent(tank, key -> new ObjectArrayList<>()).add(valve);
            }
        }

        tankValves.forEach((tank, valves) ->
        {
            FluidStack fluid = tank.getFluidInTank(0).copy();

            fluid.setAmount(fluid.getAmount() / valves.size());

            for (ValveTile valve : valves)
                valve.setStoredFluid(fluid);
        });
    }

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

    public final void markUnsaved()
    {
        if (!world.isClientSide() && world.hasChunk(mainChunk.x, mainChunk.z))
            world.getChunk(mainChunk.x, mainChunk.z).markUnsaved();
    }

    public final void updateComparatorOutputLevel()
    {
        if (!world.isClientSide())
            for (BlockPos pos : interfaces)
                if (world.isLoaded(pos))
                    world.updateNeighbourForOutputSignal(pos, world.getBlockState(pos).getBlock());
    }

    public final boolean isMainChunk(ChunkPos chunkPos)
    {
        return chunkPos.equals(mainChunk);
    }

    public final boolean isLoaded()
    {
        for (ChunkPos pos : parentChunks)
            if (!world.getChunkSource().hasChunk(pos.x, pos.z))
                return false;
        return true;
    }

    public final <MSG> void sendToTracking(MSG message)
    {
        ZYCraft.CHANNEL.send(ZYPackets.TRACKING_MULTI_BLOCK.with(() -> this), message);
    }

    public final boolean isClientSide()
    {
        return world.isClientSide();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> type, @Nullable BlockPos pos)
    {
        return LazyOptional.empty();
    }

    public void fillCrashReportCategory(CrashReportCategory category)
    {
        category.setDetail("MultiBlock Type", () -> type.getRegistryName() + " (" + getClass().getCanonicalName() + ")");
        category.setDetail("MultiBlock Main Chunk", mainChunk);
        category.setDetail("MultiBlock ID", id);
    }

    public final MultiType<?> type()
    {
        return type;
    }

    public final World world()
    {
        return world;
    }

    public final ChunkPos mainChunk()
    {
        return mainChunk;
    }

    public final ObjectList<BlockPos> childBlocks()
    {
        return childBlocks;
    }

    public final ObjectSet<ChunkPos> parentChunks()
    {
        return parentChunks;
    }

    public final int id()
    {
        return id;
    }

    public final void setID(int id)
    {
        this.id = id;
    }

    public final boolean isValid()
    {
        return valid;
    }

    protected final boolean isChanged()
    {
        return changed;
    }

    protected final void setChanged(boolean changed)
    {
        this.changed = changed;
    }

    @Override
    public final boolean equals(Object obj)
    {
        return obj == this || (obj instanceof MultiBlock && id == ((MultiBlock)obj).id);
    }

    @Override
    public final int hashCode()
    {
        return id;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type.getRegistryName())
                .add("world", world.toString() + "/" + world.dimension().location())
                .add("mainChunk", mainChunk)
                .add("id", id)
                .toString();
    }

    public enum AddingReason
    {
        FORMED,
        LOADED;

        public boolean isFormed()
        {
            return this == FORMED;
        }

        public boolean isLoaded()
        {
            return this == LOADED;
        }
    }

    public enum RemovalReason
    {
        DESTROYED,
        UNLOADED;

        public boolean isDestroyed()
        {
            return this == DESTROYED;
        }

        public boolean isUnloaded()
        {
            return this == UNLOADED;
        }
    }
}
