package nikita488.zycraft.util;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityUtils
{
    public static void register(Class<?> type)
    {
        CapabilityManager.INSTANCE.register(type, defaultStorage(), () -> null);
    }

    public static <T> Capability.IStorage<T> defaultStorage()
    {
        return (Capability.IStorage<T>)DEFAULT_STORAGE;
    }

    private static final Capability.IStorage<?> DEFAULT_STORAGE = new Capability.IStorage()
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability capability, Object instance, Direction side)
        {
            return null;
        }

        @Override
        public void readNBT(Capability capability, Object instance, Direction side, INBT tag) {}
    };
}
