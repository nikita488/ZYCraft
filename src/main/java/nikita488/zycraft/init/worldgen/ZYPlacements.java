package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.placement.Placement;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.placement.ClusterPlacement;
import nikita488.zycraft.worldgen.placement.ClusterConfig;

public class ZYPlacements
{
    public static final RegistryEntry<ClusterPlacement> QUARTZ_CRYSTAL_CLUSTER =
            ZYCraft.REGISTRY.simple("quartz_crystal_cluster", Placement.class, () -> new ClusterPlacement(ClusterConfig.CODEC));

    public static void init() {}
}
