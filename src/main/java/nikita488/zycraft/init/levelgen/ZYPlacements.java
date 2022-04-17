package nikita488.zycraft.init.levelgen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.config.ZYConfig;

import java.util.List;

public class ZYPlacements
{
    public static final PlacedFeature ZYCHORITE_VEIN = new PlacedFeature(
            Holder.direct(ZYConfiguredFeatures.ZYCHORITE_VEIN),
            List.of(CountPlacement.of(ZYConfig.zychoriteAmount),
                    InSquarePlacement.spread(),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(ZYConfig.zychoriteMinHeight), VerticalAnchor.absolute(ZYConfig.zychoriteMaxHeight)),
                    BiomeFilter.biome()));
    public static final PlacedFeature ORE_ALUMINIUM = new PlacedFeature(
            Holder.direct(ZYConfiguredFeatures.ORE_ALUMINIUM),
            List.of(CountPlacement.of(ZYConfig.aluminiumAmount),
                    InSquarePlacement.spread(),
                    HeightRangePlacement.uniform(VerticalAnchor.absolute(ZYConfig.aluminiumMinHeight), VerticalAnchor.absolute(ZYConfig.aluminiumMaxHeight)),
                    BiomeFilter.biome()));
    public static final PlacedFeature QUARTZ_CRYSTAL = new PlacedFeature(
            Holder.direct(ZYConfiguredFeatures.QUARTZ_CRYSTAL),
            List.of(CountPlacement.of(UniformInt.of(104, 157)),
                    PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                    InSquarePlacement.spread(),
                    SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13),
                    BiomeFilter.biome()));

    public static void init()
    {
        Registry<PlacedFeature> registry = BuiltinRegistries.PLACED_FEATURE;

        Registry.register(registry, ZYCraft.id("zychorite_vein"), ZYCHORITE_VEIN);
        Registry.register(registry, ZYCraft.id("ore_aluminium"), ORE_ALUMINIUM);
        Registry.register(registry, ZYCraft.id("quartz_crystal"), QUARTZ_CRYSTAL);
    }
}
