package com.nikita488.zycraft.init;

import com.nikita488.zycraft.ZYCraft;
import com.nikita488.zycraft.config.ZyConfig;
import com.nikita488.zycraft.world.gen.feature.QuartzCrystalFeature;
import com.nikita488.zycraft.world.gen.feature.ZychoriteVeinFeature;
import com.nikita488.zycraft.world.gen.feature.placement.QuartzCrystalPlacement;
import com.nikita488.zycraft.world.gen.feature.сonfig.ZychoriteVeinConfig;
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
    public static final RegistryEntry<Feature<ZychoriteVeinConfig>> ZYCHORITE_VEIN = ZYCraft.REGISTRY.object("zychorite_vein")
            .simple(Feature.class, () -> new ZychoriteVeinFeature(ZychoriteVeinConfig::deserialize));
    public static final RegistryEntry<Placement<CountRangeConfig>> QUARTZ_CRYSTAL_PLACEMENT = ZYCraft.REGISTRY.object("quartz_crystal")
            .simple(Placement.class, () -> new QuartzCrystalPlacement(CountRangeConfig::deserialize));

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
                    .withConfiguration(new ZychoriteVeinConfig(
                            ZyConfig.server().zychoriteReplaceableBlock.get(),
                            ZyConfig.server().zychoriteSize.get(),
                            ZyConfig.server().orePercentage.get() * 0.01F))
                    .withPlacement(Placement.COUNT_RANGE
                            .configure(new CountRangeConfig(
                                    ZyConfig.server().zychoriteAmountPerChunk.get(),
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
                                    ZyConfig.server().aluminiumAmountPerChunk.get(),
                                    ZyConfig.server().aluminiumMinHeight.get(),
                                    ZyConfig.server().aluminiumMinHeight.get(),
                                    ZyConfig.server().aluminiumMaxHeight.get()))));

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, new QuartzCrystalFeature(NoFeatureConfig::deserialize)
                    .withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG)
                    .withPlacement(QUARTZ_CRYSTAL_PLACEMENT.get()
                            .configure(new CountRangeConfig(20, 0, 0, 0))));
        }
    }
}
