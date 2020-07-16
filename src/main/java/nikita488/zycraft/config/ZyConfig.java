package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import nikita488.zycraft.ZYCraft;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZyConfig
{
    public static final Pair<ZyClientConfig, ForgeConfigSpec> CLIENT = new ForgeConfigSpec.Builder().configure(ZyClientConfig::new);
    public static final Pair<ZyServerConfig, ForgeConfigSpec> SERVER = new ForgeConfigSpec.Builder().configure(ZyServerConfig::new);

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT.getValue());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER.getValue());
    }

    @SubscribeEvent
    public static void reload(ModConfig.ModConfigEvent event)
    {
        ModConfig config = event.getConfig();

        if (config.getSpec() == CLIENT.getValue())
        {
            //TODO: refresh
        }
        else if (config.getSpec() == SERVER.getValue())
        {
            //TODO: refresh
        }
    }

    public static ZyClientConfig client()
    {
        return CLIENT.getKey();
    }

    public static ZyServerConfig server()
    {
        return SERVER.getKey();
    }
}
