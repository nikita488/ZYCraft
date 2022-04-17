package nikita488.zycraft;

import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import nikita488.zycraft.client.texture.CloudSprite;
import nikita488.zycraft.compat.theoneprobe.ZYProbeInfoProvider;
import nikita488.zycraft.config.ZYConfig;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;
import nikita488.zycraft.init.ZYBlockEntities;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYCreativeModeTabs;
import nikita488.zycraft.init.ZYEntities;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYMenus;
import nikita488.zycraft.init.ZYMultiTypes;
import nikita488.zycraft.init.ZYParticles;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.init.ZYTags;
import nikita488.zycraft.init.levelgen.ZYConfiguredFeatures;
import nikita488.zycraft.init.levelgen.ZYFeatures;
import nikita488.zycraft.init.levelgen.ZYPlacements;
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
        ZYBlockEntities.init();
        ZYEntities.init();
        ZYParticles.init();
        ZYFeatures.init();
        ZYMenus.init();
        ZYRegistries.init();
        ZYMultiTypes.init();

        ZYTags.init();
        ZYCreativeModeTabs.init();
        ZYLang.init();
        ZYConfig.register();
        ZYPackets.init();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(ZYConfig::init);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::enqueueIMC);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForgeClient.registerTextureAtlasSpriteLoader(ZYCraft.id("cloud"), new CloudSprite.Loader()));
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ZYConfiguredFeatures.init();
            ZYPlacements.init();
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
