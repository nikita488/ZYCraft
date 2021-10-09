package nikita488.zycraft.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import nikita488.zycraft.particle.SparkleParticleData;

import javax.annotation.Nullable;

public class SparkleParticle extends SpriteTexturedParticle
{
    private final IAnimatedSprite sprites;
    private final boolean hasFixedSize;

    public SparkleParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprites, boolean hasFixedSize)
    {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.hasFixedSize = hasFixedSize;
    }

    @Override
    public void tick()
    {
        super.tick();
        selectSpriteWithAge(sprites);
    }

    @Override
    public void selectSpriteWithAge(IAnimatedSprite sprites)
    {
        setSprite(sprites.get(age, maxAge));
    }

    public void selectSprite(int index)
    {
        setSprite(sprites.get(index, 5));
    }

    @Override
    public float getScale(float partialTicks)
    {
        return hasFixedSize ? particleScale : particleScale * (float)(maxAge - age) / (float)maxAge;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getBrightnessForRender(float partialTick)
    {
        return LightTexture.packLight(15, 15);
    }

    public void setGravity(float gravity)
    {
        this.particleGravity = gravity;
    }

    public static class Factory implements IParticleFactory<SparkleParticleData>
    {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprites)
        {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle makeParticle(SparkleParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            SparkleParticle particle = new SparkleParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, sprites, data.hasFixedSize());

            particle.setColor(data.r(), data.g(), data.b());
            particle.setAlphaF(data.a());
            particle.setMaxAge(5 * data.lifetimeFactor());
            particle.multiplyParticleScaleBy(data.sizeFactor());
            particle.setGravity(data.gravity());
            particle.canCollide = data.hasPhysics();

            if (!data.hasMotion())
                particle.motionX = particle.motionY = particle.motionZ = 0D;

            particle.selectSpriteWithAge(sprites);
            return particle;
        }
    }
}
