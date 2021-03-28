package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.model.FluidContainerModel;
import nikita488.zycraft.client.particle.SparkleParticle;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.init.ZYParticles;

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
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(ZYCraft.modLoc("fluid_container"), FluidContainerModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particles.registerFactory(ZYParticles.SPARKLE.get(), SparkleParticle.Factory::new);
    }
}
