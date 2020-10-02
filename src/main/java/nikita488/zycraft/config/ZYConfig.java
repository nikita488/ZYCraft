package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import nikita488.zycraft.ZYCraft;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZYConfig
{
    private static final Pair<ZYClientConfig, ForgeConfigSpec> CLIENT = new ForgeConfigSpec.Builder().configure(ZYClientConfig::new);
    private static final Pair<ZYCommonConfig, ForgeConfigSpec> COMMON = new ForgeConfigSpec.Builder().configure(ZYCommonConfig::new);
    //Client values

    //Common values
    public static int zychoriteSize;
    public static int zychoriteOrePercentage;
    public static int zychoriteAmount;
    public static int zychoriteMaxHeight;

    public static int aluminiumSize;
    public static int aluminiumAmount;
    public static int aluminiumMaxHeight;

    public static int quartzCrystalClusterAttempts;
    public static int quartzCrystalClusterAmount;

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT.getValue());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON.getValue());
    }

    @SubscribeEvent
    public static void init(ModConfig.ModConfigEvent event)
    {
        ModConfig config = event.getConfig();

        if (config.getSpec() == CLIENT.getValue())
        {
            ZYClientConfig client = CLIENT.getKey();
        }
        else if (config.getSpec() == COMMON.getValue())
        {
            ZYCommonConfig common = COMMON.getKey();

            zychoriteSize = common.zychoriteSize.get();
            zychoriteOrePercentage = common.zychoriteOrePercentage.get();
            zychoriteAmount = common.zychoriteAmount.get();
            zychoriteMaxHeight = common.zychoriteMaxHeight.get();

            aluminiumSize = common.aluminiumSize.get();
            aluminiumAmount = common.aluminiumAmount.get();
            aluminiumMaxHeight = common.aluminiumMaxHeight.get();

            quartzCrystalClusterAttempts = common.quartzCrystalClusterAttempts.get();
            quartzCrystalClusterAmount = common.quartzCrystalClusterAmount.get();
        }
    }
}
