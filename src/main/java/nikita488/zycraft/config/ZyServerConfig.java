package nikita488.zycraft.config;

import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ZyServerConfig
{
    public final ForgeConfigSpec.EnumValue<OreFeatureConfig.FillerBlockType> zychoriteReplaceableBlock;
    public final ForgeConfigSpec.IntValue zychoriteSize;
    public final ForgeConfigSpec.IntValue orePercentage;
    public final ForgeConfigSpec.IntValue zychoriteAmount;
    public final ForgeConfigSpec.IntValue zychoriteMinHeight;
    public final ForgeConfigSpec.IntValue zychoriteMaxHeight;

    public final ForgeConfigSpec.EnumValue<OreFeatureConfig.FillerBlockType> aluminiumReplaceableBlock;
    public final ForgeConfigSpec.IntValue aluminiumSize;
    public final ForgeConfigSpec.IntValue aluminiumAmount;
    public final ForgeConfigSpec.IntValue aluminiumMinHeight;
    public final ForgeConfigSpec.IntValue aluminiumMaxHeight;

    public final ForgeConfigSpec.IntValue quartzCrystalGenerationAttempts;
    public final ForgeConfigSpec.IntValue quartzCrystalAmount;

    public ZyServerConfig(ForgeConfigSpec.Builder builder)
    {
        builder.comment("WorldGen settings")
                .push("worldGen");

        builder.comment("Zychorite")
                .push("zychorite");
        zychoriteReplaceableBlock = builder
                .comment("Defines a block type that will be replaced by a vein")
                .defineEnum("replaceableBlock", OreFeatureConfig.FillerBlockType.NATURAL_STONE);
        zychoriteSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 17, 0, Integer.MAX_VALUE);
        orePercentage = builder
                .comment("Defines the percentage of the vein that will be filled with Zychorium Ore's")
                .defineInRange("orePercentage", 50, 0, 100);
        zychoriteAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, Integer.MAX_VALUE);
        zychoriteMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, 0, 256);
        zychoriteMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 256);
        builder.pop();

        builder.comment("Aluminium")
                .push("aluminium");
        aluminiumReplaceableBlock = builder
                .comment("Defines a block type that will be replaced by a vein")
                .defineEnum("replaceableBlock", OreFeatureConfig.FillerBlockType.NATURAL_STONE);
        aluminiumSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 8, 0, Integer.MAX_VALUE);
        aluminiumAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, Integer.MAX_VALUE);
        aluminiumMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, 0, 256);
        aluminiumMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 256);
        builder.pop();

        builder.comment("Quartz Crystal")
                .push("quartzCrystal");
        quartzCrystalGenerationAttempts = builder
                .comment("Defines the amount of attempts to generate crystals per chunk")
                .defineInRange("generationAttempts", 64, 0, Integer.MAX_VALUE);
        quartzCrystalAmount = builder
                .comment("Defines the max amount of crystals per chunk")
                .defineInRange("amount", 4, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();
    }
}
