package nikita488.zycraft.init.levelgen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.levelgen.feature.QuartzCrystalConfiguration;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinConfiguration;

public class ZYConfiguredFeatures
{
    public static final ConfiguredFeature<ZychoriteVeinConfiguration, ?> ZYCHORITE_VEIN = new ConfiguredFeature<>(
            ZYFeatures.ZYCHORITE_VEIN.get(),
            new ZychoriteVeinConfiguration(OreFeatures.NATURAL_STONE, ZYConfig.zychoriteSize, ZYConfig.zychoriteOrePercentage * 0.01F));
    public static final ConfiguredFeature<OreConfiguration, ?> ORE_ALUMINIUM = new ConfiguredFeature<>(
            Feature.ORE,
            new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), ZYConfig.aluminiumSize));
    public static final ConfiguredFeature<QuartzCrystalConfiguration, ?> QUARTZ_CRYSTAL = new ConfiguredFeature<>(
            ZYFeatures.QUARTZ_CRYSTAL.get(),
            new QuartzCrystalConfiguration(OreFeatures.NATURAL_STONE, ZYConfig.quartzCrystalMaxCrystals));

    public static void init()
    {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, ZYCraft.id("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.id("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.id("quartz_crystal"), QUARTZ_CRYSTAL);
    }
}
