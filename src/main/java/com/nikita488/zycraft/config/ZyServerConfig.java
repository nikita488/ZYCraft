package com.nikita488.zycraft.config;

import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ZyServerConfig
{
    public final ForgeConfigSpec.EnumValue<OreFeatureConfig.FillerBlockType> zychoriteReplaceableBlock;
    public final ForgeConfigSpec.IntValue zychoriteSize;
    public final ForgeConfigSpec.IntValue orePercentage;
    public final ForgeConfigSpec.IntValue zychoriteAmountPerChunk;
    public final ForgeConfigSpec.IntValue zychoriteMinHeight;
    public final ForgeConfigSpec.IntValue zychoriteMaxHeight;

    public final ForgeConfigSpec.EnumValue<OreFeatureConfig.FillerBlockType> aluminiumReplaceableBlock;
    public final ForgeConfigSpec.IntValue aluminiumSize;
    public final ForgeConfigSpec.IntValue aluminiumAmountPerChunk;
    public final ForgeConfigSpec.IntValue aluminiumMinHeight;
    public final ForgeConfigSpec.IntValue aluminiumMaxHeight;

    public final ForgeConfigSpec.IntValue quartzCrystalAmountPerChunk;
    public final ForgeConfigSpec.IntValue quartzCrystalMinHeight;
    public final ForgeConfigSpec.IntValue quartzCrystalMaxHeight;

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
        zychoriteAmountPerChunk = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amountPerChunk", 8, 0, Integer.MAX_VALUE);
        zychoriteMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, 0, 255);
        zychoriteMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 255);
        builder.pop();

        builder.comment("Aluminium")
                .push("aluminium");
        aluminiumReplaceableBlock = builder
                .comment("Defines a block type that will be replaced by a vein")
                .defineEnum("replaceableBlock", OreFeatureConfig.FillerBlockType.NATURAL_STONE);
        aluminiumSize = builder
                .comment("Defines the maximum amount of blocks in a vein")
                .defineInRange("size", 8, 0, Integer.MAX_VALUE);
        aluminiumAmountPerChunk = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amountPerChunk", 20, 0, Integer.MAX_VALUE);
        aluminiumMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, 0, 255);
        aluminiumMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 255);
        builder.pop();

        builder.comment("Quartz Crystal")
                .push("quartzCrystal");
        quartzCrystalAmountPerChunk = builder
                .comment("Defines the amount of veins per chunk")
                .defineInRange("amountPerChunk", 8, 0, Integer.MAX_VALUE);
        quartzCrystalMinHeight = builder
                .comment("Defines the minimum height at which a vein can be generated")
                .defineInRange("minHeight", 0, 0, 255);
        quartzCrystalMaxHeight = builder
                .comment("Defines the maximum height at which a vein can be generated")
                .defineInRange("maxHeight", 64, 0, 255);
        builder.pop();

        builder.pop();
    }
}
