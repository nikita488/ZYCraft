package nikita488.zycraft.init;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiType;

import java.util.function.Supplier;

public class ZYRegistries
{
    public static final Supplier<IForgeRegistry<MultiType<?>>> MULTI_TYPES = ZYCraft.registrate().makeRegistry("multi_types", MultiType.class, () ->
            new RegistryBuilder<MultiType<?>>().setMaxID(Integer.MAX_VALUE - 1).disableSaving());

    public static void init() {}
}
