package nikita488.zycraft.config;

import net.minecraft.world.gen.feature.OreFeatureConfig;
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
    public static OreFeatureConfig.FillerBlockType zychoriteReplaceableBlock;
    public static int zychoriteSize;
    public static int zychoriteOrePercentage;
    public static int zychoriteAmount;
    public static int zychoriteMinHeight;
    public static int zychoriteMaxHeight;

    public static OreFeatureConfig.FillerBlockType aluminiumReplaceableBlock;
    public static int aluminiumSize;
    public static int aluminiumAmount;
    public static int aluminiumMinHeight;
    public static int aluminiumMaxHeight;

    public static int quartzCrystalGenerationAttempts;
    public static int quartzCrystalAmount;

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

            zychoriteReplaceableBlock = common.zychoriteReplaceableBlock.get();
            zychoriteSize = common.zychoriteSize.get();
            zychoriteOrePercentage = common.zychoriteOrePercentage.get();
            zychoriteAmount = common.zychoriteAmount.get();
            zychoriteMinHeight = common.zychoriteMinHeight.get();
            zychoriteMaxHeight = common.zychoriteMaxHeight.get();

            aluminiumReplaceableBlock = common.aluminiumReplaceableBlock.get();
            aluminiumSize = common.aluminiumSize.get();
            aluminiumAmount = common.aluminiumAmount.get();
            aluminiumMinHeight = common.aluminiumMinHeight.get();
            aluminiumMaxHeight = common.aluminiumMaxHeight.get();

            quartzCrystalGenerationAttempts = common.quartzCrystalGenerationAttempts.get();
            quartzCrystalAmount = common.quartzCrystalAmount.get();
        }
    }
}
