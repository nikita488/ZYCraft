package nikita488.zycraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import nikita488.zycraft.particle.SparkleParticleData;

import javax.annotation.Nullable;

public class SparkleParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;
    private final boolean hasFixedSize;

    public SparkleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, boolean hasFixedSize)
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
    public void setSpriteFromAge(SpriteSet sprites)
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
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick)
    {
        return LightTexture.pack(15, 15);
    }

    public void setGravity(float gravity)
    {
        this.gravity = gravity;
    }

    public static class Provider implements ParticleProvider<SparkleParticleData>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites)
        {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SparkleParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            SparkleParticle sparkle = new SparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, data.hasFixedSize());

            sparkle.setColor(data.r(), data.g(), data.b());
            sparkle.setAlpha(data.a());
            sparkle.setLifetime(5 * data.lifetimeFactor());
            sparkle.scale(data.sizeFactor());
            sparkle.setGravity(data.gravity());
            sparkle.hasPhysics = data.hasPhysics();

            if (!data.hasMotion())
                sparkle.setParticleSpeed(0D, 0D, 0D);
            else
                sparkle.setParticleSpeed(xSpeed / sparkle.getLifetime(), ySpeed / sparkle.getLifetime(), zSpeed / sparkle.getLifetime());

            sparkle.setSpriteFromAge(sprites);
            return sparkle;
        }
    }
}
