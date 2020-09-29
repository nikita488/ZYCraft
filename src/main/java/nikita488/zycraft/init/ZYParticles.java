package nikita488.zycraft.init;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.particle.SparkleParticleData;

public class ZYParticles
{
    public static final RegistryEntry<ParticleType<SparkleParticleData>> SPARKLE =
            ZYCraft.REGISTRY.simple("sparkle", ParticleType.class, () -> new ZYParticleType<>(SparkleParticleData.CODEC, false, SparkleParticleData.DESERIALIZER));

    public static void init() {}

    private static final class ZYParticleType<T extends IParticleData> extends ParticleType<T>
    {
        private final Codec<T> codec;

        public ZYParticleType(Codec<T> codec, boolean alwaysShow, IParticleData.IDeserializer<T> deserializer)
        {
            super(alwaysShow, deserializer);
            this.codec = codec;
        }

        @Override
        public Codec<T> func_230522_e_()
        {
            return codec;
        }
    }
}
