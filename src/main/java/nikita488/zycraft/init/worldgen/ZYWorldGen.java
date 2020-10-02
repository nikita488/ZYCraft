package nikita488.zycraft.init.worldgen;

import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID)
public class ZYWorldGen
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addFeatures(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder builder = event.getGeneration();

        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.ZYCHORITE_VEIN);
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.ORE_ALUMINIUM);
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.QUARTZ_CRYSTAL_CLUSTER);
    }
}
