package nikita488.zycraft.init.worldgen;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.worldgen.feature.ClusterFeatureConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.worldgen.placement.ClusterPlacementConfig;

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
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL_CLUSTER = ZYFeatures.QUARTZ_CRYSTAL_CLUSTER.get()
            .configured(new ClusterFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, 5))
            .decorated(ZYPlacements.QUARTZ_CRYSTAL_CLUSTER.get().configured(new ClusterPlacementConfig(ZYConfig.quartzCrystalClusterAttempts, ZYConfig.quartzCrystalClusterAmount)));

    public static void init()
    {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, ZYCraft.modLoc("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.modLoc("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.modLoc("quartz_crystal_cluster"), QUARTZ_CRYSTAL_CLUSTER);
    }
}
