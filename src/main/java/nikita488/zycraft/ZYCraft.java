package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import nikita488.zycraft.compat.theoneprobe.ZYProbeInfoProvider;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;
import nikita488.zycraft.init.*;
import nikita488.zycraft.init.worldgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.worldgen.ZYFeatures;
import nikita488.zycraft.init.worldgen.ZYPlacements;
import nikita488.zycraft.multiblock.MultiManager;
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
        ZYEntities.init();
        ZYParticles.init();
        ZYFeatures.init();
        ZYPlacements.init();
        ZYContainers.init();
        ZYRegistries.init();
        ZYMultiTypes.init();

        ZYTags.init();
        ZYGroups.init();
        ZYLang.init();
        ZYConfig.register();
        ZYPackets.init();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(ZYConfig::init);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::enqueueIMC);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ZYConfiguredFeatures.init();
            DispenserBlock.registerBehavior(ZYItems.QUARTZ_BUCKET.get(), ZYBucketDispenseItemBehavior.INSTANCE);
        });
        MultiManager.commonSetup();
    }

    private void enqueueIMC(InterModEnqueueEvent event)
    {
        if (ModList.get().isLoaded("theoneprobe"))
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", ZYProbeInfoProvider::new);
    }

    public static Registrate registrate()
    {
        return REGISTRATE.get();
    }

    public static ResourceLocation id(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public static String string(String name)
    {
        return MOD_ID + ":" + name;
    }
}
