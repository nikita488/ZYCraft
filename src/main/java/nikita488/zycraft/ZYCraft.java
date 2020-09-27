package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.*;
import nikita488.zycraft.init.worldgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.worldgen.ZYFeatures;
import nikita488.zycraft.init.worldgen.ZYPlacements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Mod(ZYCraft.MOD_ID)
public class ZYCraft
{
    public static final String MOD_ID = "zycraft";
    public static final Logger LOGGER = LogManager.getLogger();
    public static Registrate REGISTRY;

    public ZYCraft()
    {
        REGISTRY = Registrate.create(MOD_ID);
        ZYBlocks.init();
        ZYItems.init();
        ZYTags.init();
        ZYTiles.init();
        ZYParticles.init();
        ZYGroups.init();
        ZYDamageSources.init();
        ZYConfig.register();
        ZYTextComponents.init();

        ZYFeatures.init();
        ZYPlacements.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        //event.enqueueWork(ZYConfiguredFeatures::init);
        for (Map.Entry<RegistryKey<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> entry : WorldGenRegistries.CONFIGURED_FEATURE.getEntries())
        {
            System.out.println(entry.getKey().getLocation());
        }
    }

    public static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
