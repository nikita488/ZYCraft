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
    public static final ConfiguredFeature<?, ?> ZYCHORITE_VEIN = ZYFeatures.ZYCHORITE_VEIN.get()
            .configured(new ZychoriteVeinConfiguration(OreFeatures.NATURAL_STONE, ZYConfig.zychoriteSize, ZYConfig.zychoriteOrePercentage * 0.01F));
    public static final ConfiguredFeature<?, ?> ORE_ALUMINIUM = Feature.ORE
            .configured(new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), ZYConfig.aluminiumSize));
    public static final ConfiguredFeature<?, ?> QUARTZ_CRYSTAL = ZYFeatures.QUARTZ_CRYSTAL.get()
            .configured(new QuartzCrystalConfiguration(OreFeatures.NATURAL_STONE, ZYConfig.quartzCrystalMaxCrystals));

    public static void init()
    {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, ZYCraft.id("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.id("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.id("quartz_crystal"), QUARTZ_CRYSTAL);
    }
}
