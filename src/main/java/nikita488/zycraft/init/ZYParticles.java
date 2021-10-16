package nikita488.zycraft.init;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.particle.SparkleParticleData;

public class ZYParticles
{
    public static final RegistryEntry<ParticleType<SparkleParticleData>> SPARKLE = ZYCraft.registrate().simple("sparkle", ParticleType.class, () ->
            new ZYParticleType<>(SparkleParticleData.CODEC, false, SparkleParticleData.DESERIALIZER));

    public static void init() {}

    private static final class ZYParticleType<T extends ParticleOptions> extends ParticleType<T>
    {
        private final Codec<T> codec;

        public ZYParticleType(Codec<T> codec, boolean overrideLimiter, ParticleOptions.Deserializer<T> deserializer)
        {
            super(overrideLimiter, deserializer);
            this.codec = codec;
        }

        @Override
        public Codec<T> codec()
        {
            return codec;
        }
    }
}
