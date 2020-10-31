package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.NonNullLazyValue;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;
import nikita488.zycraft.init.*;
import nikita488.zycraft.init.worldgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.worldgen.ZYFeatures;
import nikita488.zycraft.init.worldgen.ZYPlacements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZYCraft.MOD_ID)
public class ZYCraft
{
    public static final String MOD_ID = "zycraft";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<>(() -> Registrate.create(MOD_ID));

    public ZYCraft()
    {
        ZYBlocks.init();
        ZYItems.init();
        ZYTiles.init();
        ZYParticles.init();
        ZYFeatures.init();
        ZYPlacements.init();

        ZYTags.init();
        ZYGroups.init();
        ZYLang.init();
        ZYConfig.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ZYConfiguredFeatures.init();
            DispenserBlock.registerDispenseBehavior(ZYItems.QUARTZ_BUCKET.get(), ZYBucketDispenseItemBehavior.INSTANCE);
        });
    }

    public static Registrate registrate()
    {
        return REGISTRATE.get();
    }

    public static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
