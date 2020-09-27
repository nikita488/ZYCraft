package nikita488.zycraft.init.worldgen;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.worldgen.placement.ClusterConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;

public class ZYConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> ZYCHORITE_VEIN = register("zychorite_vein",
            ZYFeatures.ZYCHORITE_VEIN.get().withConfiguration(
                    new ZychoriteVeinConfig(
                            OreFeatureConfig.FillerBlockType.field_241882_a,
                            17,
                            0.5F))
                    .func_242733_d(64)
                    .func_242728_a()
                    .func_242731_b(8));
    public static final ConfiguredFeature<?, ?> ORE_ALUMINIUM = register("ore_aluminium",
            Feature.ORE.withConfiguration(
                    new OreFeatureConfig(
                            OreFeatureConfig.FillerBlockType.field_241882_a,
                            ZYBlocks.ALUMINIUM_ORE.getDefaultState(),
                            8))
                    .func_242733_d(64)
                    .func_242728_a()
                    .func_242731_b(8));
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL_CLUSTER = register("quartz_crystal_cluster",
            ZYFeatures.QUARTZ_CRYSTAL_CLUSTER.get()
                    .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(ZYPlacements.QUARTZ_CRYSTAL_CLUSTER.get()
                            .configure(new ClusterConfig(64, 4))));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> feature)
    {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, feature);
    }

    public static void init() {}
}
