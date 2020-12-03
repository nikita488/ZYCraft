package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.WidgetTextureAtlas;
import nikita488.zycraft.client.model.FluidContainerModel;
import nikita488.zycraft.client.particle.SparkleParticle;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.init.ZYParticles;
import nikita488.zycraft.multiblock.MultiChildType;
import nikita488.zycraft.multiblock.client.MultiChildModel;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ZYClientSetup
{
    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ItemModelsProperties.registerProperty(ZYItems.ALUMINIUM_FOIL.get(), ZYCraft.modLoc("filled"), (stack, world, entity) ->
            {
                FluidStack containedFluid = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
                return containedFluid.getAmount() / 16000.0F;
            });
        });

        for (MultiChildType type : MultiChildType.VALUES)
            RenderTypeLookup.setRenderLayer(ZYBlocks.DEFAULT_MULTI_CHILD.get(type).get(), layer -> true);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(ZYCraft.modLoc("fluid_container"), FluidContainerModel.Loader.INSTANCE);
        ModelLoaderRegistry.registerLoader(ZYCraft.modLoc("multi_child"), MultiChildModel.Loader.INSTANCE);
    }

    public static final NonNullLazy<WidgetTextureAtlas> WIDGET_ATLAS = NonNullLazy.of(WidgetTextureAtlas::new);

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        mc.particles.registerFactory(ZYParticles.SPARKLE.get(), SparkleParticle.Factory::new);
        ((IReloadableResourceManager)mc.getResourceManager()).addReloadListener(WIDGET_ATLAS.get());
    }

    public static WidgetTextureAtlas getWidgetAtlas()
    {
        return WIDGET_ATLAS.get();
    }
}
