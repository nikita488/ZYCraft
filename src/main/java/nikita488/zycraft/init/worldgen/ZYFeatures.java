package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.feature.Feature;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.feature.QuartzCrystalFeature;
import nikita488.zycraft.worldgen.feature.QuartzCrystalFeatureConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinFeature;

public class ZYFeatures
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.registrate().simple("zychorite_vein", Feature.class, () ->
            new ZychoriteVeinFeature(ZychoriteVeinConfig.CODEC));
    public static final RegistryEntry<QuartzCrystalFeature> QUARTZ_CRYSTAL = ZYCraft.registrate().simple("quartz_crystal_cluster", Feature.class, () ->
            new QuartzCrystalFeature(QuartzCrystalFeatureConfig.CODEC));

    public static void init() {}
}
