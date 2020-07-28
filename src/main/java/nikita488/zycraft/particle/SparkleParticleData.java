package nikita488.zycraft.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import nikita488.zycraft.init.ZYParticles;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class SparkleParticleData implements IParticleData
{
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
            boolean canCollide = reader.readBoolean();
            reader.expect(' ');
            boolean staticScale = reader.readBoolean();
            reader.expect(' ');
            boolean zeroMotion = reader.readBoolean();
            return new SparkleParticleData(color, ageFactor, scaleFactor, gravity, canCollide, staticScale, zeroMotion);
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
    private final boolean canCollide;
    private final boolean staticScale;
    private final boolean zeroMotion;

    public SparkleParticleData(int rgba, int ageFactor, float scaleFactor, float gravity, boolean canCollide, boolean staticScale, boolean zeroMotion)
    {
        this.color = rgba;
        this.ageFactor = ageFactor;
        this.scaleFactor = scaleFactor;
        this.gravity = gravity;
        this.canCollide = canCollide;
        this.staticScale = staticScale;
        this.zeroMotion = zeroMotion;
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
        buffer.writeBoolean(canCollide);
        buffer.writeBoolean(staticScale);
        buffer.writeBoolean(zeroMotion);
    }

    @Override
    public String getParameters()
    {
        return "";//TODO: String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", getType().getRegistryName(), setR, setG, setB, a);
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

    public boolean canCollide()
    {
        return canCollide;
    }

    public boolean staticScale()
    {
        return staticScale;
    }

    public boolean zeroMotion()
    {
        return zeroMotion;
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
        private boolean canCollide = true;
        private boolean staticScale;
        private boolean zeroMotion;

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
            this.canCollide = false;
            return this;
        }

        public Builder staticScale()
        {
            this.staticScale = true;
            return this;
        }

        public Builder zeroMotion()
        {
            this.zeroMotion = true;
            return this;
        }

        public SparkleParticleData build()
        {
            return new SparkleParticleData(color, ageFactor, scaleFactor, gravity, canCollide, staticScale, zeroMotion);
        }
    }
}
