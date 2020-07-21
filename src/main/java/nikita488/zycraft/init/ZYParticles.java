package nikita488.zycraft.init;

import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.particle.SparkleParticleData;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.ParticleType;

public class ZYParticles
{
    public static final RegistryEntry<ParticleType<SparkleParticleData>> SPARKLE = ZYCraft.REGISTRY.object("sparkle")
            .simple(ParticleType.class, () -> new ParticleType<>(false, SparkleParticleData.DESERIALIZER));

    public static void init() {}
}
