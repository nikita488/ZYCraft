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
    public static final Codec<SparkleParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(data -> data.color),
            Codec.INT.fieldOf("age_factor").forGetter(data -> data.ageFactor),
            Codec.FLOAT.fieldOf("scale_factor").forGetter(data -> data.scaleFactor),
            Codec.FLOAT.fieldOf("gravity").forGetter(data -> data.gravity),
            Codec.BOOL.fieldOf("collidable").forGetter(data -> data.collidable),
            Codec.BOOL.fieldOf("scalable").forGetter(data -> data.scalable),
            Codec.BOOL.fieldOf("motionless").forGetter(data -> data.motionless))
            .apply(instance, SparkleParticleData::new));

    public static final IParticleData.IDeserializer<SparkleParticleData> DESERIALIZER = new IParticleData.IDeserializer<SparkleParticleData>()
    {
        @Override
        public SparkleParticleData deserialize(ParticleType<SparkleParticleData> type, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            int ageFactor = reader.readInt();
            reader.expect(' ');
            float scaleFactor = reader.readFloat();
            reader.expect(' ');
            float gravity = reader.readFloat();
            reader.expect(' ');
            boolean collidable = reader.readBoolean();
            reader.expect(' ');
            boolean scalable = reader.readBoolean();
            reader.expect(' ');
            boolean motionless = reader.readBoolean();
            return new SparkleParticleData(color, ageFactor, scaleFactor, gravity, collidable, scalable, motionless);
        }

        @Override
        public SparkleParticleData read(ParticleType<SparkleParticleData> type, PacketBuffer buffer)
        {
            return new SparkleParticleData(buffer.readVarInt(), buffer.readVarInt(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
        }
    };
    private final int color;
    private final int ageFactor;
    private final float scaleFactor;
    private final float gravity;
    private final boolean collidable;
    private final boolean scalable;
    private final boolean motionless;

    public SparkleParticleData(int rgba, int ageFactor, float scaleFactor, float gravity, boolean collidable, boolean scalable, boolean motionless)
    {
        this.color = rgba;
        this.ageFactor = ageFactor;
        this.scaleFactor = scaleFactor;
        this.gravity = gravity;
        this.collidable = collidable;
        this.scalable = scalable;
        this.motionless = motionless;
    }

    @Override
    public ParticleType<SparkleParticleData> getType()
    {
        return ZYParticles.SPARKLE.get();
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeVarInt(color);
        buffer.writeVarInt(ageFactor);
        buffer.writeFloat(scaleFactor);
        buffer.writeFloat(gravity);
        buffer.writeBoolean(collidable);
        buffer.writeBoolean(scalable);
        buffer.writeBoolean(motionless);
    }

    @Override
    public String getParameters()
    {
        return getType().getRegistryName().toString();
    }

    public float r()
    {
        return ((color >> 24) & 0xFF) / 255.0F;
    }

    public float g()
    {
        return ((color >> 16) & 0xFF) / 255.0F;
    }

    public float b()
    {
        return ((color >> 8) & 0xFF) / 255.0F;
    }

    public float a()
    {
        return (color & 0xFF) / 255.0F;
    }

    public int ageFactor()
    {
        return ageFactor;
    }

    public float scaleFactor()
    {
        return scaleFactor;
    }

    public float gravity()
    {
        return gravity;
    }

    public boolean collidable()
    {
        return collidable;
    }

    public boolean scalable()
    {
        return scalable;
    }

    public boolean motionless()
    {
        return motionless;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private int color = 0xFFFFFFBF;
        private int ageFactor = 1;
        private float scaleFactor = 1;
        private float gravity;
        private boolean collidable = true;
        private boolean scalable = true;
        private boolean motionless;

        public Builder color(int rgba)
        {
            this.color = rgba;
            return this;
        }

        public Builder ageFactor(int factor)
        {
            this.ageFactor = factor;
            return this;
        }

        public Builder scaleFactor(float factor)
        {
            this.scaleFactor = factor;
            return this;
        }

        public Builder gravity(float gravity)
        {
            this.gravity = gravity;
            return this;
        }

        public Builder noClip()
        {
            this.collidable = false;
            return this;
        }

        public Builder fixedScale()
        {
            this.scalable = false;
            return this;
        }

        public Builder motionless()
        {
            this.motionless = true;
            return this;
        }

        public SparkleParticleData build()
        {
            return new SparkleParticleData(color, ageFactor, scaleFactor, gravity, collidable, scalable, motionless);
        }
    }
}
