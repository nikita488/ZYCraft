package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.levelgen.feature.QuartzCrystalConfiguration;
import nikita488.zycraft.levelgen.feature.QuartzCrystalFeature;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinConfiguration;
import nikita488.zycraft.levelgen.feature.ZychoriteVeinFeature;

public class ZYFeatures
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.registrate().simple("zychorite_vein", ForgeRegistries.Keys.FEATURES, () ->
            new ZychoriteVeinFeature(ZychoriteVeinConfiguration.CODEC));
    public static final RegistryEntry<QuartzCrystalFeature> QUARTZ_CRYSTAL = ZYCraft.registrate().simple("quartz_crystal", ForgeRegistries.Keys.FEATURES, () ->
            new QuartzCrystalFeature(QuartzCrystalConfiguration.CODEC));

    public static void init() {}
}
