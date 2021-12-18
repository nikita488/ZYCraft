package nikita488.zycraft.init.levelgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.levelgen.feature.Feature;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.levelgen.feature.QuartzCrystalConfiguration;
import nikita488.zycraft.levelgen.feature.QuartzCrystalFeature;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinConfiguration;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinFeature;

public class ZYFeatures
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.registrate().simple("zychorite_vein", Feature.class, () ->
            new ZychoriteVeinFeature(ZychoriteVeinConfiguration.CODEC));
    public static final RegistryEntry<QuartzCrystalFeature> QUARTZ_CRYSTAL = ZYCraft.registrate().simple("quartz_crystal", Feature.class, () ->
            new QuartzCrystalFeature(QuartzCrystalConfiguration.CODEC));

    public static void init() {}
}
