package nikita488.zycraft.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYFeatures;
import nikita488.zycraft.levelgen.feature.QuartzCrystalConfiguration;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinConfiguration;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ZYDataGenerator
{
    private static final ResourceLocation ZYCHORITE_VEIN = ZYCraft.id("zychorite_vein");
    private static final ResourceLocation ORE_ALUMINIUM = ZYCraft.id("ore_aluminium");
    private static final ResourceLocation QUARTZ_CRYSTAL = ZYCraft.id("quartz_crystal");

    private static Map<ResourceLocation, ConfiguredFeature<?, ?>> getConfiguredFeatures()
    {
        ImmutableMap.Builder<ResourceLocation, ConfiguredFeature<?, ?>> builder = ImmutableMap.builder();

        builder.put(ZYCHORITE_VEIN, new ConfiguredFeature<>(
                ZYFeatures.ZYCHORITE_VEIN.get(),
                new ZychoriteVeinConfiguration(OreFeatures.NATURAL_STONE, 17, 0.5F)));
        builder.put(ORE_ALUMINIUM, new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, ZYBlocks.ALUMINIUM_ORE.getDefaultState(), 8)));
        builder.put(QUARTZ_CRYSTAL, new ConfiguredFeature<>(
                ZYFeatures.QUARTZ_CRYSTAL.get(),
                new QuartzCrystalConfiguration(OreFeatures.NATURAL_STONE, 5)));
        return builder.build();
    }

    private static Map<ResourceLocation, PlacedFeature> getPlacedFeatures(RegistryOps<JsonElement> ops)
    {
        ImmutableMap.Builder<ResourceLocation, PlacedFeature> builder = ImmutableMap.builder();
        Registry<ConfiguredFeature<?, ?>> configuredFeatures = ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get();

        builder.put(ZYCHORITE_VEIN, new PlacedFeature(
                configuredFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, ZYCHORITE_VEIN)),
                List.of(CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome())));
        builder.put(ORE_ALUMINIUM, new PlacedFeature(
                configuredFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, ORE_ALUMINIUM)),
                List.of(CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome())));
        builder.put(QUARTZ_CRYSTAL, new PlacedFeature(
                configuredFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, QUARTZ_CRYSTAL)),
                List.of(CountPlacement.of(UniformInt.of(104, 157)),
                        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                        InSquarePlacement.spread(),
                        SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13),
                        BiomeFilter.biome())));
        return builder.build();
    }

    private static Map<ResourceLocation, BiomeModifier> getBiomeModifiers(RegistryOps<JsonElement> ops)
    {
        ImmutableMap.Builder<ResourceLocation, BiomeModifier> builder = ImmutableMap.builder();
        Registry<PlacedFeature> placedFeatures = ops.registry(Registry.PLACED_FEATURE_REGISTRY).get();
        HolderSet.Named<Biome> biomes = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), BiomeTags.IS_OVERWORLD);

        builder.put(ZYCHORITE_VEIN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(placedFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, ZYCHORITE_VEIN))),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        builder.put(ORE_ALUMINIUM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(placedFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, ORE_ALUMINIUM))),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        builder.put(QUARTZ_CRYSTAL, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(placedFeatures.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, QUARTZ_CRYSTAL))),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        return builder.build();
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        JsonCodecProvider<ConfiguredFeature<?, ?>> configuredFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, ZYCraft.MOD_ID, ops, Registry.CONFIGURED_FEATURE_REGISTRY, getConfiguredFeatures());
        JsonCodecProvider<PlacedFeature> placedFeatureProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, ZYCraft.MOD_ID, ops, Registry.PLACED_FEATURE_REGISTRY, getPlacedFeatures(ops));
        JsonCodecProvider<BiomeModifier> biomeModifierProvider = JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, ZYCraft.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, getBiomeModifiers(ops));

        generator.addProvider(event.includeServer(), configuredFeatureProvider);
        generator.addProvider(event.includeServer(), placedFeatureProvider);
        generator.addProvider(event.includeServer(), biomeModifierProvider);
    }
}
