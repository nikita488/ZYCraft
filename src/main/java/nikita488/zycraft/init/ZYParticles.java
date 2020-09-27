package nikita488.zycraft.init;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.ParticleType;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.particle.SparkleParticleData;

public class ZYParticles
{
    public static final RegistryEntry<ParticleType<SparkleParticleData>> SPARKLE = ZYCraft.REGISTRY.object("sparkle")
            .simple(ParticleType.class, () -> new ParticleType<SparkleParticleData>(false, SparkleParticleData.DESERIALIZER)
            {
                @Override
                public Codec func_230522_e_()
                {
                    return SparkleParticleData.CODEC;
                }
            });

    public static void init() {}
}
