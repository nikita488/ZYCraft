package nikita488.zycraft.client.particle;

import nikita488.zycraft.particle.SparkleParticleData;
import net.minecraft.client.particle.*;
import net.minecraft.world.World;

public class SparkleParticle extends SpriteTexturedParticle
{
    private final IAnimatedSprite sprites;
    private final boolean staticScale;

    public SparkleParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, IAnimatedSprite sprites, boolean staticScale)
    {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.staticScale = staticScale;
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
        return staticScale ? particleScale : particleScale * (float)(maxAge - age) / (float)maxAge;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getBrightnessForRender(float partialTick)
    {
        return 0xF000F0;
    }

    public void setGravity(float gravity)
    {
        particleGravity = gravity;
    }

    public static class Factory implements IParticleFactory<SparkleParticleData>
    {
        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite sprites)
        {
            this.sprites = sprites;
        }

        public Particle makeParticle(SparkleParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            SparkleParticle particle = new SparkleParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, sprites, data.staticScale());
            particle.setColor(data.r(), data.g(), data.b());
            particle.setAlphaF(data.a());
            particle.setMaxAge(5 * data.ageFactor());
            particle.multiplyParticleScaleBy(data.scaleFactor());
            particle.setGravity(data.gravity());
            particle.canCollide = data.canCollide();
            if (data.zeroMotion())
                particle.motionX = particle.motionY = particle.motionZ = 0;
            particle.selectSpriteWithAge(sprites);
            return particle;
        }
    }
}
