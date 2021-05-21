package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;
import nikita488.zycraft.init.*;
import nikita488.zycraft.init.worldgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.worldgen.ZYFeatures;
import nikita488.zycraft.init.worldgen.ZYPlacements;
import nikita488.zycraft.network.ZYPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZYCraft.MOD_ID)
public class ZYCraft
{
    public static final String MOD_ID = "zycraft";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(MOD_ID));
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(ZYCraft.id("main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(version -> version.equals(PROTOCOL_VERSION))
            .serverAcceptedVersions(version -> version.equals(PROTOCOL_VERSION))
            .simpleChannel();

    public ZYCraft()
    {
        ZYBlocks.init();
        ZYItems.init();
        ZYTiles.init();
        ZYParticles.init();
        ZYFeatures.init();
        ZYPlacements.init();
        ZYContainers.init();

        ZYTags.init();
        ZYGroups.init();
        ZYLang.init();
        ZYConfig.register();
        ZYPackets.init();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::commonSetup);
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

    public static ResourceLocation id(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }
}
