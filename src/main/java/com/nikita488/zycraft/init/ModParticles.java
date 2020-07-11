package com.nikita488.zycraft.init;

import com.nikita488.zycraft.ZYCraft;
import com.nikita488.zycraft.particle.SparkleParticleData;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.particles.ParticleType;

public class ModParticles
{
    public static final RegistryEntry<ParticleType<SparkleParticleData>> SPARKLE = ZYCraft.REGISTRY.object("sparkle")
            .simple(ParticleType.class, () -> new ParticleType<>(false, SparkleParticleData.DESERIALIZER));

    public static void init() {}
}
