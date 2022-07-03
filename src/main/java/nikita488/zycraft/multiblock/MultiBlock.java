package nikita488.zycraft.multiblock;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;
import nikita488.zycraft.network.ZYPackets;
import nikita488.zycraft.util.Cuboid6i;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultiBlock
{
    private static final AtomicInteger NEXT_MULTI_ID = new AtomicInteger();
    final MultiType<?> type;
    final MultiManager manager;
    final Level level;
    final ChunkPos mainChunk;
    final ObjectList<BlockPos> childBlocks = new ObjectArrayList<>();
    final ObjectList<BlockPos> interfaces = new ObjectArrayList<>();
    final ObjectSet<ChunkPos> parentChunks = new ObjectOpenHashSet<>();
    private int id;
    boolean valid;
    private boolean changed;

    public MultiBlock(MultiType<?> type, Level level, ChunkPos mainChunk)
    {
        this.type = type;
        this.manager = MultiManager.getInstance(level);
        this.level = level;
        this.mainChunk = mainChunk;
        this.id = !level.isClientSide() ? NEXT_MULTI_ID.incrementAndGet() : 0;
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
            if (level.isLoaded(pos) && level.getBlockEntity(pos) instanceof IMultiChild child)
            {
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
        if (!level.isClientSide() && reason.isDestroyed())
            onDestroy();

        this.valid = false;

        for (BlockPos pos : childBlocks)
        {
            if (level.isLoaded(pos) && level.getBlockEntity(pos) instanceof IMultiChild child)
            {
                child.onMultiInvalidation(this);
                onChildInvalidation(child);
            }
        }

        invalidateCapabilities();
    }

    public void onChildValidation(IMultiChild child) {}
    public void onChildInvalidation(IMultiChild child) {}

    public abstract void initChildBlocks();

    public InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return InteractionResult.PASS;
    }

    public int getLightValue(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return state.getLightEmission();
    }

    public final void sendMultiUpdated()
    {
        manager.sendMultiUpdated(this);
    }

    protected final void splitFluids()
    {
        Object2ObjectMap<IFluidHandler, ObjectList<ValveBlockEntity>> tankValves = new Object2ObjectArrayMap<>();

        for (BlockPos pos : interfaces)
        {
            if (!level.isLoaded(pos))
                continue;

            LazyOptional<IFluidHandler> capability = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, pos);
            IFluidHandler tank = capability.orElse(EmptyFluidHandler.INSTANCE);

            if (capability.isPresent() && !tank.getFluidInTank(0).isEmpty() && level.getBlockEntity(pos) instanceof ValveBlockEntity valve && !valve.hasStoredFluid())
                tankValves.computeIfAbsent(tank, key -> new ObjectArrayList<>()).add(valve);
        }

        tankValves.forEach((tank, valves) ->
        {
            FluidStack fluid = tank.getFluidInTank(0).copy();

            fluid.setAmount(fluid.getAmount() / valves.size());

            for (ValveBlockEntity valve : valves)
                valve.setStoredFluid(fluid);
        });
    }

    public void encode(FriendlyByteBuf buffer) {}
    public void decode(FriendlyByteBuf buffer) {}

    public void encodeUpdate(FriendlyByteBuf buffer)
    {
        encode(buffer);
    }

    public void decodeUpdate(FriendlyByteBuf buffer)
    {
        decode(buffer);
    }

    public void save(CompoundTag tag) {}
    public void load(CompoundTag tag) {}

    public final void setUnsaved()
    {
        if (!level.isClientSide() && level.hasChunk(mainChunk.x, mainChunk.z))
            level.getChunk(mainChunk.x, mainChunk.z).setUnsaved(true);
    }

    public final void updateInterfacesForOutputSignal()
    {
        if (!level.isClientSide())
            for (BlockPos pos : interfaces)
                if (level.isLoaded(pos))
                    level.updateNeighbourForOutputSignal(pos, level.getBlockState(pos).getBlock());
    }

    public final boolean isMainChunk(ChunkPos chunkPos)
    {
        return chunkPos.equals(mainChunk);
    }

    public final boolean isLoaded()
    {
        for (ChunkPos pos : parentChunks)
            if (!level.getChunkSource().hasChunk(pos.x, pos.z))
                return false;
        return true;
    }

    public final <MSG> void sendToTracking(MSG message)
    {
        ZYCraft.CHANNEL.send(ZYPackets.TRACKING_MULTI_BLOCK.with(() -> this), message);
    }

    public final boolean isClientSide()
    {
        return level.isClientSide();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> type, @Nullable BlockPos pos)
    {
        return LazyOptional.empty();
    }

    public void fillCrashReportCategory(CrashReportCategory category)
    {
        category.setDetail("MultiBlock Type", () -> ZYRegistries.MULTI_TYPES.get().getKey(type) + " (" + getClass().getCanonicalName() + ")");
        category.setDetail("MultiBlock Main Chunk", mainChunk);
        category.setDetail("MultiBlock ID", id);
    }

    public final MultiType<?> type()
    {
        return type;
    }

    public final Level level()
    {
        return level;
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
        return obj == this || (obj instanceof MultiBlock multiBlock && id == multiBlock.id);
    }

    @Override
    public final int hashCode()
    {
        return id;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", ZYRegistries.MULTI_TYPES.get().getKey(type))
                .add("level", level.toString() + "/" + level.dimension().location())
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
