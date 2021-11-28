package nikita488.zycraft.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import nikita488.zycraft.particle.SparkleParticleData;

import javax.annotation.Nullable;

public class SparkleParticle extends SpriteTexturedParticle
{
    private final IAnimatedSprite sprites;
    private final boolean hasFixedSize;

    public SparkleParticle(ClientWorld level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprites, boolean hasFixedSize)
    {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.hasFixedSize = hasFixedSize;
    }

    @Override
    public void tick()
    {
        super.tick();
        setSpriteFromAge(sprites);
    }

    @Override
    public void setSpriteFromAge(IAnimatedSprite sprites)
    {
        setSprite(sprites.get(age, lifetime));
    }

    public void selectSprite(int index)
    {
        setSprite(sprites.get(index, 5));
    }

    @Override
    public float getQuadSize(float partialTicks)
    {
        return hasFixedSize ? quadSize : quadSize * (float)(lifetime - age) / (float)lifetime;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick)
    {
        return LightTexture.pack(15, 15);
    }

    public void setParticleSpeed(double x, double y, double z)
    {
        this.xd = x / lifetime;
        this.yd = y / lifetime;
        this.zd = z / lifetime;
    }

    public void setGravity(float gravity)
    {
        this.gravity = gravity;
    }

    public static class Provider implements IParticleFactory<SparkleParticleData>
    {
        private final IAnimatedSprite sprites;

        public Provider(IAnimatedSprite sprites)
        {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SparkleParticleData data, ClientWorld level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            SparkleParticle sparkle = new SparkleParticle(level, x, y, z, 0D, 0D, 0D, sprites, data.hasFixedSize());

            sparkle.setColor(data.r(), data.g(), data.b());
            sparkle.setAlpha(data.a());
            sparkle.setLifetime(5 * data.lifetimeFactor());
            sparkle.scale(data.sizeFactor());
            sparkle.setGravity(data.gravity());
            sparkle.hasPhysics = data.hasPhysics();

            if (!data.hasMotion())
                sparkle.setParticleSpeed(0D, 0D, 0D);
            else
                sparkle.setParticleSpeed(xSpeed, ySpeed, zSpeed);

            sparkle.setSpriteFromAge(sprites);
            return sparkle;
        }
    }
}
