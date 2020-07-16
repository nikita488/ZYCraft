package nikita488.zycraft.init;

import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZyConfig;
import nikita488.zycraft.world.gen.feature.QuartzCrystalFeature;
import nikita488.zycraft.world.gen.feature.ZychoriteVeinFeature;
import nikita488.zycraft.world.gen.feature.placement.QuartzCrystalPlacement;
import nikita488.zycraft.world.gen.feature.placement.config.QuartzCrystalPlacementConfig;
import nikita488.zycraft.world.gen.feature.сonfig.ZychoriteVeinFeatureConfig;
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

public class ModWorldGen
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.REGISTRY.object("zychorite_vein")
            .simple(Feature.class, () -> new ZychoriteVeinFeature(ZychoriteVeinFeatureConfig::deserialize));
    public static final RegistryEntry<QuartzCrystalFeature> QUARTZ_CRYSTAL = ZYCraft.REGISTRY.object("quartz_crystal")
            .simple(Feature.class, () -> new QuartzCrystalFeature(NoFeatureConfig::deserialize));
    public static final RegistryEntry<QuartzCrystalPlacement> QUARTZ_CRYSTAL_PLACEMENT = ZYCraft.REGISTRY.object("quartz_crystal")
            .simple(Placement.class, () -> new QuartzCrystalPlacement(QuartzCrystalPlacementConfig::deserialize));

    public static void init() {}

    public static void addFeature()
    {
        for (Biome biome : ForgeRegistries.BIOMES)
        {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID) ||
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) ||
                    BiomeDictionary.hasType(biome, BiomeDictionary.Type.END))
                continue;
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYCHORITE_VEIN.get()
                    .withConfiguration(new ZychoriteVeinFeatureConfig(
                            ZyConfig.server().zychoriteReplaceableBlock.get(),
                            ZyConfig.server().zychoriteSize.get(),
                            ZyConfig.server().orePercentage.get() * 0.01F))
                    .withPlacement(Placement.COUNT_RANGE
                            .configure(new CountRangeConfig(
                                    ZyConfig.server().zychoriteAmount.get(),
                                    ZyConfig.server().zychoriteMinHeight.get(),
                                    ZyConfig.server().zychoriteMinHeight.get(),
                                    ZyConfig.server().zychoriteMaxHeight.get()))));

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                    .withConfiguration(new OreFeatureConfig(
                            ZyConfig.server().aluminiumReplaceableBlock.get(),
                            ModBlocks.ALUMINIUM_ORE.getDefaultState(),
                            ZyConfig.server().aluminiumSize.get()))
                    .withPlacement(Placement.COUNT_RANGE
                            .configure(new CountRangeConfig(
                                    ZyConfig.server().aluminiumAmount.get(),
                                    ZyConfig.server().aluminiumMinHeight.get(),
                                    ZyConfig.server().aluminiumMinHeight.get(),
                                    ZyConfig.server().aluminiumMaxHeight.get()))));

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, QUARTZ_CRYSTAL.get()
                    .withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(QUARTZ_CRYSTAL_PLACEMENT.get()
                            .configure(new QuartzCrystalPlacementConfig(
                                    ZyConfig.server().quartzCrystalGenerationAttempts.get(),
                                    ZyConfig.server().quartzCrystalAmount.get()))));
        }
    }
}
