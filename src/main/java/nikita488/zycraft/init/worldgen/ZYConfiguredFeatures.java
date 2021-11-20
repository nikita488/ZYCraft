package nikita488.zycraft.init.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.levelgen.feature.ClusterFeatureConfig;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.levelgen.placement.ClusterPlacementConfig;

public class ZYConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> ZYCHORITE_VEIN = ZYFeatures.ZYCHORITE_VEIN.get()
            .configured(new ZychoriteVeinConfig(OreConfiguration.Predicates.NATURAL_STONE, ZYConfig.zychoriteSize, ZYConfig.zychoriteOrePercentage * 0.01F))
            .rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(ZYConfig.zychoriteMaxHeight))
            .squared()
            .count(ZYConfig.zychoriteAmount);
    public static final ConfiguredFeature<?, ?> ORE_ALUMINIUM = Feature.ORE
            .configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), ZYConfig.aluminiumSize))
            .rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(ZYConfig.aluminiumMaxHeight))
            .squared()
            .count(ZYConfig.aluminiumAmount);
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL_CLUSTER = ZYFeatures.QUARTZ_CRYSTAL_CLUSTER.get()
            .configured(new ClusterFeatureConfig(OreConfiguration.Predicates.NATURAL_STONE, 5))
            .decorated(ZYPlacements.QUARTZ_CRYSTAL_CLUSTER.get().configured(new ClusterPlacementConfig(ZYConfig.quartzCrystalClusterAttempts, ZYConfig.quartzCrystalClusterAmount)));

    public static void init()
    {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, ZYCraft.id("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.id("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.id("quartz_crystal_cluster"), QUARTZ_CRYSTAL_CLUSTER);
    }
}
