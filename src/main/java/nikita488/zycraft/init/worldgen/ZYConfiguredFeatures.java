package nikita488.zycraft.init.worldgen;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.worldgen.feature.ClusterFeatureConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.worldgen.placement.ClusterPlacementConfig;

public class ZYConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> ZYCHORITE_VEIN = ZYFeatures.ZYCHORITE_VEIN.get()
            .withConfiguration(new ZychoriteVeinConfig(OreFeatureConfig.FillerBlockType.field_241882_a, ZYConfig.zychoriteSize, ZYConfig.zychoriteOrePercentage * 0.01F))
            .func_242733_d(ZYConfig.zychoriteMaxHeight)
            .func_242728_a()
            .func_242731_b(ZYConfig.zychoriteAmount);
    public static final ConfiguredFeature<?, ?> ORE_ALUMINIUM = Feature.ORE
            .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), ZYConfig.aluminiumSize))
            .func_242733_d(ZYConfig.aluminiumMaxHeight)
            .func_242728_a()
            .func_242731_b(ZYConfig.aluminiumAmount);
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL_CLUSTER = ZYFeatures.QUARTZ_CRYSTAL_CLUSTER.get()
            .withConfiguration(new ClusterFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, 5))
            .withPlacement(ZYPlacements.QUARTZ_CRYSTAL_CLUSTER.get().configure(new ClusterPlacementConfig(ZYConfig.quartzCrystalClusterAttempts, ZYConfig.quartzCrystalClusterAmount)));
}
