package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.particle.SparkleParticle;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYParticles;
import nikita488.zycraft.multiblock.child.DefaultMultiChildModel;
import nikita488.zycraft.api.multiblock.child.MultiChildMaterial;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup
{
    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event)
    {
        ModelLoaderRegistry.registerLoader(ZYCraft.modLoc("default_multi_child"), new DefaultMultiChildModel.Loader());
        for (MultiChildMaterial material : MultiChildMaterial.VALUES)
            RenderTypeLookup.setRenderLayer(ZYBlocks.DEFAULT_MULTI_CHILD.get(material).get(), renderType -> true);
    }

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particles.registerFactory(ZYParticles.SPARKLE.get(), SparkleParticle.Factory::new);
    }
}
