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
    private static final Pair<ZYServerConfig, ForgeConfigSpec> SERVER = new ForgeConfigSpec.Builder().configure(ZYServerConfig::new);
    //Client values

    //Server values
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER.getValue());
    }

    @SubscribeEvent
    public static void reload(ModConfig.ModConfigEvent event)
    {
        ModConfig config = event.getConfig();

        if (config.getSpec() == CLIENT.getValue())
        {
            ZYClientConfig client = CLIENT.getKey();
        }
        else if (config.getSpec() == SERVER.getValue())
        {
            ZYServerConfig server = SERVER.getKey();
            zychoriteReplaceableBlock = server.zychoriteReplaceableBlock.get();
            zychoriteSize = server.zychoriteSize.get();
            zychoriteOrePercentage = server.zychoriteOrePercentage.get();
            zychoriteAmount = server.zychoriteAmount.get();
            zychoriteMinHeight = server.zychoriteMinHeight.get();
            zychoriteMaxHeight = server.zychoriteMaxHeight.get();

            aluminiumReplaceableBlock = server.aluminiumReplaceableBlock.get();
            aluminiumSize = server.aluminiumSize.get();
            aluminiumAmount = server.aluminiumAmount.get();
            aluminiumMinHeight = server.aluminiumMinHeight.get();
            aluminiumMaxHeight = server.aluminiumMaxHeight.get();

            quartzCrystalGenerationAttempts = server.quartzCrystalGenerationAttempts.get();
            quartzCrystalAmount = server.quartzCrystalAmount.get();
        }
    }
}
