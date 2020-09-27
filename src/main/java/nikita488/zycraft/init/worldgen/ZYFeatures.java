package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.feature.ClusterFeature;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinFeature;
import nikita488.zycraft.worldgen.feature.ZychoriteVeinConfig;

public class ZYFeatures
{
    public static final RegistryEntry<ZychoriteVeinFeature> ZYCHORITE_VEIN = ZYCraft.REGISTRY.object("zychorite_vein")
            .simple(Feature.class, () -> new ZychoriteVeinFeature(ZychoriteVeinConfig.CODEC));
    public static final RegistryEntry<ClusterFeature> QUARTZ_CRYSTAL_CLUSTER = ZYCraft.REGISTRY.object("quartz_crystal_cluster")
            .simple(Feature.class, () -> new ClusterFeature(NoFeatureConfig.field_236558_a_));

    public static void init() {}
}
