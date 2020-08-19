package nikita488.zycraft.api.multiblock;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nikita488.zycraft.api.multiblock.capability.IMultiChunk;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.api.util.Cuboid6i;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.MultiCapabilities;
import nikita488.zycraft.multiblock.child.DefaultMultiChildBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger NEXT_MULTI_ID = new AtomicInteger();
    protected final MultiType type;
    protected IMultiWorld world;
    protected IMultiChunk mainChunk;
    private int id = NEXT_MULTI_ID.getAndIncrement();
    protected boolean valid;
    private boolean needsUpdate;
    protected final ObjectArrayList<BlockPos> childBlocks = new ObjectArrayList<>();
    protected final ObjectOpenHashSet<ChunkPos> parentChunks = new ObjectOpenHashSet<>();

    public MultiBlock(MultiType type)
    {
        this.type = type;
    }

    public MultiBlock(MultiType type, IWorld world, ChunkPos mainChunk)
    {
        this(type);
        this.world = MultiCapabilities.getMultiWorld(world).orElse(null);
        this.mainChunk = this.world.getChunk(mainChunk).orElse(null);
    }

    @Nullable
    public static MultiBlock create(CompoundNBT tag)
    {
        String name = tag.getString("Type");
        return Optional.ofNullable(ZYRegistries.multiType().getValue(new ResourceLocation(name))).map((type) ->
        {
            try
            {
                return type.get();
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Failed to create MultiBlock {}", name, throwable);
                return null;
            }
        }).map((multiBlock) ->
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
            LOGGER.warn("Skipping MultiBlock with id {}", name);
            return null;
        });
    }

    public void addChildBlocks(Cuboid6i cuboid)
    {
        addChildBlocks(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
    }

    public void addChildBlocks(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    addChildBlock(new BlockPos(x, y, z));
    }

    public void addChildBlock(BlockPos pos)
    {
        childBlocks.add(pos);
        parentChunks.add(new ChunkPos(pos));
    }

    public void validate(MultiValidationType type)
    {
        if (childBlocks.isEmpty())
            return;

        if (!world.isRemote())
            for (BlockPos pos : childBlocks)
                if (world.getMultiChild(pos) == null && !DefaultMultiChildBlock.tryConvertAt(world.parent(), pos))
                    return;

        for (BlockPos pos : childBlocks)
        {
            MultiChildTile child = world.getMultiChild(pos);
            if (child == null) continue;
            child.onMultiValidation(this, type);
            onChildValidation(child, type);
        }

        valid = true;
    }

    public void invalidate(MultiInvalidationType type)
    {
        valid = false;

        for (BlockPos pos : childBlocks)
        {
            MultiChildTile child = world.getMultiChild(pos);
            if (child == null) continue;
            child.onMultiInvalidation(this, type);
            onChildInvalidation(child, type);
        }
    }

    public void onChildValidation(MultiChildTile child, MultiValidationType type) {}
    public void onChildInvalidation(MultiChildTile child, MultiInvalidationType type) {}

    public int getChildLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return 0;
    }

    public boolean canInteractWith(PlayerEntity player)
    {
        MultiChildTile child = world.getMultiChild(childBlocks.get(0));
        return child != null && child.canInteractWith(player);
    }

    public ActionResultType onChildInteraction(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
    }

    public void markDirty()
    {
        mainChunk.markDirty();
    }

    public void scheduleUpdate()
    {
        world.scheduleMultiUpdate(this);
    }

    public abstract void initChildBlocks();

    public void encode(PacketBuffer buffer) {}
    public void decode(PacketBuffer buffer) {}

    public void encodeUpdate(PacketBuffer buffer) {}
    public void decodeUpdate(PacketBuffer buffer) {}

    public void save(CompoundNBT tag) {}
    public void load(CompoundNBT tag) {}

    public boolean dynamic()
    {
        return false;//this instanceof ITickableMultiBlock || this instanceof IRenderableMultiBlock;
    }

    @Nullable
    public BlockPos origin()
    {
        return !childBlocks.isEmpty() ? childBlocks.get(0) : null;
    }

    public MultiType type()
    {
        return type;
    }

    public IMultiWorld world()
    {
        return world;
    }

    public IMultiChunk mainChunk()
    {
        return mainChunk;
    }

    public boolean isMainChunk(ChunkPos pos)
    {
        return pos.equals(mainChunk.pos());
    }

    public int id()
    {
        return id;
    }

    public boolean valid()
    {
        return valid;
    }

    public boolean needsUpdate()
    {
        return needsUpdate;
    }

    public ObjectArrayList<BlockPos> childBlocks()
    {
        return childBlocks;
    }

    public ObjectOpenHashSet<ChunkPos> parentChunks()
    {
        return parentChunks;
    }

    public boolean isLoaded()
    {
        for (ChunkPos pos : parentChunks)
            if (!world.isChunkLoaded(pos))
                return false;

        return true;
    }

    public void setLocation(IMultiWorld world, ChunkPos pos)
    {
        setLocation(world, world.getChunk(pos).orElse(null));
    }

    public void setLocation(IMultiWorld world, IMultiChunk parentChunk)
    {
        this.world = world;
        this.mainChunk = parentChunk;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public void setNeedsUpdate(boolean needsUpdate)
    {
        this.needsUpdate = needsUpdate;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return id == ((MultiBlock)obj).id;
    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
