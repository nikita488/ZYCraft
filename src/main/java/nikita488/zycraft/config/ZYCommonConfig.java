package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZYCommonConfig
{
    protected final ForgeConfigSpec.IntValue zychoriteSize;
    protected final ForgeConfigSpec.IntValue zychoriteOrePercentage;
    protected final ForgeConfigSpec.IntValue zychoriteAmount;
    protected final ForgeConfigSpec.IntValue zychoriteMaxHeight;

    protected final ForgeConfigSpec.IntValue aluminiumSize;
    protected final ForgeConfigSpec.IntValue aluminiumAmount;
    protected final ForgeConfigSpec.IntValue aluminiumMaxHeight;

    protected final ForgeConfigSpec.IntValue quartzCrystalClusterAttempts;
    protected final ForgeConfigSpec.IntValue quartzCrystalClusterAmount;

    public ZYCommonConfig(ForgeConfigSpec.Builder builder)
    {
        builder.comment("WorldGen settings")
                .push("worldGen");

        builder.comment("Zychorite")
                .push("zychorite");
        zychoriteSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 17, 0, Integer.MAX_VALUE);
        zychoriteOrePercentage = builder
                .comment("Defines the percentage of the vein that will be filled with Zychorium Ore's")
                .defineInRange("orePercentage", 50, 0, 100);
        zychoriteAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, Integer.MAX_VALUE);
        zychoriteMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 256);
        builder.pop();

        builder.comment("Aluminium")
                .push("aluminium");
        aluminiumSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 8, 0, Integer.MAX_VALUE);
        aluminiumAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, Integer.MAX_VALUE);
        aluminiumMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 256);
        builder.pop();

        builder.comment("Quartz Crystal Cluster")
                .push("quartzCrystalCluster");
        quartzCrystalClusterAttempts = builder
                .comment("Defines the amount of attempts to generate crystal clusters per chunk")
                .defineInRange("generationAttempts", 64, 0, Integer.MAX_VALUE);
        quartzCrystalClusterAmount = builder
                .comment("Defines the max amount of crystal clusters per chunk")
                .defineInRange("amount", 4, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();
    }
}
