package nikita488.zycraft.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import nikita488.zycraft.init.ZYParticles;

public class SparkleParticleData implements IParticleData
{
    public static final Codec<SparkleParticleData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Codec.INT.fieldOf("color").forGetter(data -> data.color),
                    Codec.INT.fieldOf("lifetime_factor").forGetter(data -> data.lifetimeFactor),
                    Codec.FLOAT.fieldOf("size_factor").forGetter(data -> data.sizeFactor),
                    Codec.FLOAT.fieldOf("gravity").forGetter(data -> data.gravity),
                    Codec.BOOL.fieldOf("has_physics").forGetter(data -> data.hasPhysics),
                    Codec.BOOL.fieldOf("has_motion").forGetter(data -> data.hasMotion),
                    Codec.BOOL.fieldOf("has_fixed_size").forGetter(data -> data.hasFixedSize))
            .apply(instance, SparkleParticleData::new));

    public static final IParticleData.IDeserializer<SparkleParticleData> DESERIALIZER = new IParticleData.IDeserializer<SparkleParticleData>()
    {
        @Override
        public SparkleParticleData fromCommand(ParticleType<SparkleParticleData> type, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            int lifetimeFactor = reader.readInt();
            reader.expect(' ');
            float sizeFactor = reader.readFloat();
            reader.expect(' ');
            float gravity = reader.readFloat();
            reader.expect(' ');
            boolean hasPhysics = reader.readBoolean();
            reader.expect(' ');
            boolean hasMotion = reader.readBoolean();
            reader.expect(' ');
            boolean hasFixedSize = reader.readBoolean();
            return new SparkleParticleData(color, lifetimeFactor, sizeFactor, gravity, hasPhysics, hasMotion, hasFixedSize);
        }

        @Override
        public SparkleParticleData fromNetwork(ParticleType<SparkleParticleData> type, PacketBuffer buffer)
        {
            return new SparkleParticleData(buffer.readVarInt(), buffer.readVarInt(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
        }
    };
    private final int color;
    private final int lifetimeFactor;
    private final float sizeFactor;
    private final float gravity;
    private final boolean hasPhysics;
    private final boolean hasMotion;
    private final boolean hasFixedSize;

    public SparkleParticleData(int rgba, int lifetimeFactor, float sizeFactor, float gravity, boolean hasPhysics, boolean hasMotion, boolean hasFixedSize)
    {
        this.color = rgba;
        this.lifetimeFactor = lifetimeFactor;
        this.sizeFactor = sizeFactor;
        this.gravity = gravity;
        this.hasPhysics = hasPhysics;
        this.hasMotion = hasMotion;
        this.hasFixedSize = hasFixedSize;
    }

    @Override
    public ParticleType<SparkleParticleData> getType()
    {
        return ZYParticles.SPARKLE.get();
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer)
    {
        buffer.writeVarInt(color);
        buffer.writeVarInt(lifetimeFactor);
        buffer.writeFloat(sizeFactor);
        buffer.writeFloat(gravity);
        buffer.writeBoolean(hasPhysics);
        buffer.writeBoolean(hasMotion);
        buffer.writeBoolean(hasFixedSize);
    }

    @Override
    public String writeToString()
    {
        return getType().getRegistryName().toString();
    }

    public float r()
    {
        return ((color >> 24) & 255) / 255F;
    }

    public float g()
    {
        return ((color >> 16) & 255) / 255F;
    }

    public float b()
    {
        return ((color >> 8) & 255) / 255F;
    }

    public float a()
    {
        return (color & 255) / 255F;
    }

    public int lifetimeFactor()
    {
        return lifetimeFactor;
    }

    public float sizeFactor()
    {
        return sizeFactor;
    }

    public float gravity()
    {
        return gravity;
    }

    public boolean hasPhysics()
    {
        return hasPhysics;
    }

    public boolean hasMotion()
    {
        return hasMotion;
    }

    public boolean hasFixedSize()
    {
        return hasFixedSize;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private int color = 0xFFFFFFBF;
        private int lifetimeFactor = 1;
        private float sizeFactor = 1F;
        private float gravity;
        private boolean hasPhysics, hasMotion, hasFixedSize;

        public Builder color(int rgba)
        {
            this.color = rgba;
            return this;
        }

        public Builder lifetimeFactor(int factor)
        {
            this.lifetimeFactor = factor;
            return this;
        }

        public Builder sizeFactor(float factor)
        {
            this.sizeFactor = factor;
            return this;
        }

        public Builder gravity(float gravity)
        {
            this.gravity = gravity;
            return this;
        }

        public Builder hasPhysics()
        {
            this.hasPhysics = true;
            return this;
        }

        public Builder hasMotion()
        {
            this.hasMotion = true;
            return this;
        }

        public Builder hasFixedSize()
        {
            this.hasFixedSize = true;
            return this;
        }

        public SparkleParticleData build()
        {
            return new SparkleParticleData(color, lifetimeFactor, sizeFactor, gravity, hasPhysics, hasMotion, hasFixedSize);
        }
    }
}
