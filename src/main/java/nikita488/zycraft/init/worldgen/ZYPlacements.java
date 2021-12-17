package nikita488.zycraft.init.worldgen;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.gen.placement.Placement;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.worldgen.placement.QuartzCrystalPlacement;
import nikita488.zycraft.worldgen.placement.QuartzCrystalPlacementConfig;

public class ZYPlacements
{
    public static final RegistryEntry<QuartzCrystalPlacement> QUARTZ_CRYSTAL = ZYCraft.registrate().simple("quartz_crystal_cluster", Placement.class, () ->
            new QuartzCrystalPlacement(QuartzCrystalPlacementConfig.CODEC));

    public static void init() {}
}
