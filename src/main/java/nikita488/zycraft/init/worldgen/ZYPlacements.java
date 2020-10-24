package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.placement.Placement;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.placement.ClusterPlacement;
import nikita488.zycraft.worldgen.placement.ClusterPlacementConfig;

public class ZYPlacements
{
    public static final RegistryEntry<ClusterPlacement> QUARTZ_CRYSTAL_CLUSTER = ZYCraft.registrate().simple("quartz_crystal_cluster", Placement.class, () ->
            new ClusterPlacement(ClusterPlacementConfig.CODEC));

    public static void init() {}
}
