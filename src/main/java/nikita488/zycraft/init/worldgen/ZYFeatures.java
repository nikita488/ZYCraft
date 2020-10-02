package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.feature.Feature;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.feature.ClusterFeature;
import nikita488.zycraft.worldgen.feature.ClusterFeatureConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinFeature;

public class ZYFeatures
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.REGISTRY.simple("zychorite_vein", Feature.class, () ->
            new ZychoriteVeinFeature(ZychoriteVeinConfig.CODEC));
    public static final RegistryEntry<ClusterFeature> QUARTZ_CRYSTAL_CLUSTER = ZYCraft.REGISTRY.simple("quartz_crystal_cluster", Feature.class, () ->
            new ClusterFeature(ClusterFeatureConfig.CODEC));

    public static void init() {}
}
