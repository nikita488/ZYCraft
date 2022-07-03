package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class ZYConfig
{
    private static final Pair<ZYClientConfig, ForgeConfigSpec> CLIENT = new ForgeConfigSpec.Builder().configure(ZYClientConfig::new);
    private static final Pair<ZYCommonConfig, ForgeConfigSpec> COMMON = new ForgeConfigSpec.Builder().configure(ZYCommonConfig::new);
    //Client values

    //Common values

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT.getValue());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON.getValue());
    }

    public static void init(ModConfigEvent event)
    {
        ModConfig config = event.getConfig();

        if (config.getSpec() == CLIENT.getValue())
        {
            ZYClientConfig client = CLIENT.getKey();
        }
        else if (config.getSpec() == COMMON.getValue())
        {
            ZYCommonConfig common = COMMON.getKey();
        }
    }
}
