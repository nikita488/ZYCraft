package nikita488.zycraft.init.worldgen;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.worldgen.feature.QuartzCrystalFeatureConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.worldgen.placement.QuartzCrystalPlacementConfig;

public class ZYConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> ZYCHORITE_VEIN = ZYFeatures.ZYCHORITE_VEIN.get()
            .configured(new ZychoriteVeinConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ZYConfig.zychoriteSize, ZYConfig.zychoriteOrePercentage * 0.01F))
            .range(ZYConfig.zychoriteMaxHeight)
            .squared()
            .count(ZYConfig.zychoriteAmount);
    public static final ConfiguredFeature<?, ?> ORE_ALUMINIUM = Feature.ORE
            .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), ZYConfig.aluminiumSize))
            .range(ZYConfig.aluminiumMaxHeight)
            .squared()
            .count(ZYConfig.aluminiumAmount);
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL = ZYFeatures.QUARTZ_CRYSTAL.get()
            .configured(new QuartzCrystalFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ZYConfig.quartzCrystalMaxCrystals))
            .decorated(ZYPlacements.QUARTZ_CRYSTAL.get().configured(new QuartzCrystalPlacementConfig(ZYConfig.quartzCrystalAttempts, ZYConfig.quartzCrystalAmount)));

    public static void init()
    {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, ZYCraft.id("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.id("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.id("quartz_crystal_cluster"), QUARTZ_CRYSTAL);
    }
}
