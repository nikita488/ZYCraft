package nikita488.zycraft.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiType;

import java.util.function.Supplier;

public class ZYRegistries
{
    private static final DeferredRegister<MultiType<?>> DEFERRED_MULTI_TYPES = DeferredRegister.create(Keys.MULTI_TYPES, ZYCraft.MOD_ID);
    public static final Supplier<IForgeRegistry<MultiType<?>>> MULTI_TYPES = DEFERRED_MULTI_TYPES.makeRegistry(() ->
            new RegistryBuilder<MultiType<?>>().setMaxID(Integer.MAX_VALUE - 1).disableSaving());

    public static void init()
    {
        Keys.init();
        DEFERRED_MULTI_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final class Keys
    {
        public static final ResourceKey<Registry<MultiType<?>>> MULTI_TYPES = key("multi_types");

        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(ZYCraft.id(name));
        }

        private static void init() {}
    }
}
