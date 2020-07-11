package com.nikita488.zycraft;

import com.nikita488.zycraft.config.ZyConfig;
import com.nikita488.zycraft.init.*;
import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZYCraft.MOD_ID)
public class ZYCraft
{
    public static final String MOD_ID = "zycraft";
    public static final Logger LOGGER = LogManager.getLogger();
    public static Registrate REGISTRY;

    public ZYCraft()
    {
        REGISTRY = Registrate.create(MOD_ID);
        ModBlocks.init();
        ModItems.init();
        ModTags.init();
        ModTiles.init();
        ModParticles.init();
        ModGroups.init();
        ModDamageSources.init();
        ModWorldGen.init();
        ZyConfig.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(ModWorldGen::addFeature);
    }

    public static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
