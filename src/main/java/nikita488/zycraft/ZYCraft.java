package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.init.*;
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
        ZYBlocks.init();
        ZYItems.init();
        ZYTags.init();
        ZYTiles.init();
        ZYParticles.init();
        ZYGroups.init();
        ZYDamageSources.init();
        ZYWorldGen.init();
        ZYConfig.register();
        ZYTextComponents.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(ZYWorldGen::addFeatures);
    }

    public static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
