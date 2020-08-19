package nikita488.zycraft.multiblock.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.api.multiblock.capability.IMultiChunk;
import nikita488.zycraft.util.CapabilityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiChunkCapability
{
    @CapabilityInject(IMultiChunk.class)
    public static final Capability<IMultiChunk> INSTANCE = null;

    public static void register()
    {
        CapabilityUtils.register(IMultiChunk.class);
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        private final MultiChunk chunk;
        private final LazyOptional<IMultiChunk> capability;

        public Provider(Chunk chunk)
        {
            this.chunk = new MultiChunk(chunk);
            this.capability = LazyOptional.of(() -> this.chunk);
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            chunk.save(tag);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            chunk.load(tag);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return INSTANCE.orEmpty(cap, capability);
        }
    }
}
