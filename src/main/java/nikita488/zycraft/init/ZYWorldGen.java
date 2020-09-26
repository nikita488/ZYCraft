package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.world.gen.feature.QuartzCrystalClusterFeature;
import nikita488.zycraft.world.gen.feature.ZychoriteVeinFeature;
import nikita488.zycraft.world.gen.feature.placement.QuartzCrystalClusterPlacement;
import nikita488.zycraft.world.gen.feature.placement.config.QuartzCrystalClusterPlacementConfig;
import nikita488.zycraft.world.gen.feature.сonfig.ZychoriteVeinFeatureConfig;

public class ZYWorldGen
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.REGISTRY.object("zychorite_vein")
            .simple(Feature.class, () -> new ZychoriteVeinFeature(ZychoriteVeinFeatureConfig::deserialize));
    public static final RegistryEntry<QuartzCrystalClusterFeature> QUARTZ_CRYSTAL_CLUSTER = ZYCraft.REGISTRY.object("quartz_crystal")
            .simple(Feature.class, () -> new QuartzCrystalClusterFeature(NoFeatureConfig::deserialize));
    public static final RegistryEntry<QuartzCrystalClusterPlacement> QUARTZ_CRYSTAL_CLUSTER_PLACEMENT = ZYCraft.REGISTRY.object("quartz_crystal")
            .simple(Placement.class, () -> new QuartzCrystalClusterPlacement(QuartzCrystalClusterPlacementConfig::deserialize));

    public static void init() {}

    public static void addFeatures()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID) ||
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) ||
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.END))
                continue;
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYCHORITE_VEIN.get()
                    .withConfiguration(new ZychoriteVeinFeatureConfig(
                            ZYConfig.zychoriteReplaceableBlock,
                            ZYConfig.zychoriteSize,
                            ZYConfig.zychoriteOrePercentage * 0.01F))
                    .withPlacement(Placement.COUNT_RANGE
                            .configure(new CountRangeConfig(
                                    ZYConfig.zychoriteAmount,
                                    ZYConfig.zychoriteMinHeight,
                                    ZYConfig.zychoriteMinHeight,
                                    ZYConfig.zychoriteMaxHeight))));

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                    .withConfiguration(new OreFeatureConfig(
                            ZYConfig.aluminiumReplaceableBlock,
                            ZYBlocks.ALUMINIUM_ORE.getDefaultState(),
                            ZYConfig.aluminiumSize))
                    .withPlacement(Placement.COUNT_RANGE
                            .configure(new CountRangeConfig(
                                    ZYConfig.aluminiumAmount,
                                    ZYConfig.aluminiumMinHeight,
                                    ZYConfig.aluminiumMinHeight,
                                    ZYConfig.aluminiumMaxHeight))));

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, QUARTZ_CRYSTAL_CLUSTER.get()
                    .withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(QUARTZ_CRYSTAL_CLUSTER_PLACEMENT.get()
                            .configure(new QuartzCrystalClusterPlacementConfig(
                                    ZYConfig.quartzCrystalClusterAttempts,
                                    ZYConfig.quartzCrystalClusterAmount))));
        }
    }
}
