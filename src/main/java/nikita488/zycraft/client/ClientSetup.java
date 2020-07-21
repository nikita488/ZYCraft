package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.particle.SparkleParticle;
import nikita488.zycraft.init.ZYParticles;
import nikita488.zycraft.util.Color4b;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup
{
    public static final Color4b[] mcColors = new Color4b[]{Color4b.from(-1), Color4b.from(255, 130, 0, 255), Color4b.from(-16711681), Color4b.from(105, 195, 255, 255), Color4b.from(-65281), Color4b.from(16711935), Color4b.from(255, 135, 255, 255), Color4b.from(-2139062017), Color4b.from(-1061109505), Color4b.from(16777215), Color4b.from(150, 0, 250, 255), Color4b.from('\uffff'), Color4b.from(110, 65, 0, 255), Color4b.from(8388863), Color4b.from(-16776961), Color4b.from(741092607)};
    public static int[] colors = new int[]
    {
            0xFFFFFF,
            0xFF8000,
            0xFF00FF,
            -1,
            0xFFFF00,
            0x00FF00,
            0xFF80FF,
            0x00FF00,
            0xC0C0C0,
            0x00FFFF,
            -1,
            0x0000FF,
            -1,
            0x008000,
            0xFF0000,
            -1
    };

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particles.registerFactory(ZYParticles.SPARKLE.get(), SparkleParticle.Factory::new);
    }
}
