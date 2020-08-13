package nikita488.zycraft.multiblock.capability;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.util.CapabilityUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiWorldCapability
{
    @CapabilityInject(IMultiWorld.class)
    public static final Capability<IMultiWorld> INSTANCE = null;

    public static void register()
    {
        CapabilityUtils.register(IMultiWorld.class);
    }

    public static class Provider implements ICapabilityProvider
    {
        private final MultiWorld world;
        private final LazyOptional<IMultiWorld> capability;

        public Provider(World world)
        {
            this.world = new MultiWorld(world);
            this.capability = LazyOptional.of(() -> this.world);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return INSTANCE.orEmpty(cap, capability);
        }
    }
}
