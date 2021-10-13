package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.model.ConvertedMultiChildModel;
import nikita488.zycraft.client.model.FluidContainerModel;
import nikita488.zycraft.client.particle.SparkleParticle;
import nikita488.zycraft.client.renderer.multiblock.MultiHighlightRenderer;
import nikita488.zycraft.client.texture.CloudSprite;
import nikita488.zycraft.client.texture.GuiComponentManager;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.init.ZYParticles;
import nikita488.zycraft.multiblock.MultiManager;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ZYClientSetup
{
    private static GuiComponentManager guiComponentManager;

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event)
    {
        MultiManager.clientSetup();
        MultiHighlightRenderer.init();
        MinecraftForgeClient.registerTextureAtlasSpriteLoader(ZYCraft.id("cloud"), new CloudSprite.Loader());

        event.enqueueWork(() ->
        {
            ItemModelsProperties.register(ZYItems.ALUMINIUM_FOIL.get(), ZYCraft.id("filled"), (stack, world, entity) ->
            {
                FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);

                if (fluidStack.isEmpty())
                    return 0F;
                else if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack))
                    return 1F;
                else
                    return (float)fluidStack.getAmount() / 16000;
            });
        });
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(ZYCraft.id("fluid_container"), FluidContainerModel.Loader.INSTANCE);
        ModelLoaderRegistry.registerLoader(ZYCraft.id("converted_multi_child"), ConvertedMultiChildModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void register(ParticleFactoryRegisterEvent event)
    {
        Minecraft mc = Minecraft.getInstance();

        mc.particleEngine.register(ZYParticles.SPARKLE.get(), SparkleParticle.Factory::new);
        ((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(guiComponentManager = new GuiComponentManager(mc.getTextureManager()));
    }

    public static GuiComponentManager guiComponentManager()
    {
        return guiComponentManager;
    }
}
