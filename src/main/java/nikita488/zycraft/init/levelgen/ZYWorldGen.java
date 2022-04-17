package nikita488.zycraft.init.levelgen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
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
        ResourceLocation name = event.getName();

        if (name == null)
            return;

        ResourceKey<Biome> biome = ResourceKey.create(Registry.BIOME_REGISTRY, name);

        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID))
            return;

        BiomeGenerationSettingsBuilder builder = event.getGeneration();

        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(ZYPlacements.ZYCHORITE_VEIN));
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(ZYPlacements.ORE_ALUMINIUM));
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(ZYPlacements.QUARTZ_CRYSTAL));
    }
}
