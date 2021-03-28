package nikita488.zycraft.init.worldgen;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
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
        RegistryKey<Biome> biome = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());

        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) ||
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID))
            return;

        BiomeGenerationSettingsBuilder builder = event.getGeneration();

        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.ZYCHORITE_VEIN);
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.ORE_ALUMINIUM);
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ZYConfiguredFeatures.QUARTZ_CRYSTAL_CLUSTER);
    }
}
