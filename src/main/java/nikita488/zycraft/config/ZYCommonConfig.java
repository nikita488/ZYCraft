package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZYCommonConfig
{
    protected final ForgeConfigSpec.IntValue zychoriteSize;
    protected final ForgeConfigSpec.IntValue zychoriteOrePercentage;
    protected final ForgeConfigSpec.IntValue zychoriteAmount;
    protected final ForgeConfigSpec.IntValue zychoriteMinHeight;
    protected final ForgeConfigSpec.IntValue zychoriteMaxHeight;

    protected final ForgeConfigSpec.IntValue aluminiumSize;
    protected final ForgeConfigSpec.IntValue aluminiumAmount;
    protected final ForgeConfigSpec.IntValue aluminiumMinHeight;
    protected final ForgeConfigSpec.IntValue aluminiumMaxHeight;

    protected final ForgeConfigSpec.IntValue quartzCrystalMaxCrystals;

    public ZYCommonConfig(ForgeConfigSpec.Builder builder)
    {
        builder.comment("WorldGen settings").push("worldGen");

        builder.comment("Zychorite").push("zychorite");
        this.zychoriteSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 17, 0, 64);
        this.zychoriteOrePercentage = builder
                .comment("Defines the percentage of the vein that will be filled with Zychorium Ore's")
                .defineInRange("orePercentage", 50, 0, 100);
        this.zychoriteAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, 256);
        this.zychoriteMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, -64, 256);
        this.zychoriteMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, -64, 256);
        builder.pop();

        builder.comment("Aluminium").push("aluminium");
        this.aluminiumSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 8, 0, 64);
        this.aluminiumAmount = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amount", 8, 0, 256);
        this.aluminiumMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, -64, 256);
        this.aluminiumMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, -64, 256);
        builder.pop();

        builder.comment("Quartz Crystal").push("quartzCrystal");
        this.quartzCrystalMaxCrystals = builder
                .comment("Defines the maximum amount of crystals per cluster")
                .defineInRange("maxCrystals", 5, 1, 5);
        builder.pop();

        builder.pop();
    }
}
