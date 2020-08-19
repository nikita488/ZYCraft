package nikita488.zycraft.init;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.MultiType;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZYRegistries
{
    private static IForgeRegistry<MultiType> MULTI_TYPE;

    @SubscribeEvent
    public static void create(RegistryEvent.NewRegistry event)
    {
        MULTI_TYPE = new RegistryBuilder<MultiType>()
                .setName(ZYCraft.modLoc("multi_type"))
                .setType(MultiType.class)
                .setMaxID(Integer.MAX_VALUE - 1)
                .disableSaving()
                .create();

        ZYMultiBlocks.init();
    }

    public static IForgeRegistry<MultiType> multiType()
    {
        return MULTI_TYPE;
    }
}
