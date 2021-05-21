package nikita488.zycraft.client.texture;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.util.Color;
import nikita488.zycraft.util.IntBiConsumer;

import java.util.Random;

public class CloudSprite extends TextureAtlasSprite
{
    public static final ResourceLocation NAME = ZYCraft.id("cloud");
    public static final ResourceLocation NAME2 = ZYCraft.id("cloud2");
    private static final int[] OFFSETS = new int[] {0, -1, 0, 1};
    private static final Random RANDOM = new Random();
    private final float[] pixels, baseLayer, adjustmentLayer;

    public CloudSprite(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipMapLevels, int atlasWidth, int atlasHeight, int x, int y, NativeImage image)
    {
        super(atlas, info, 0, atlasWidth, atlasHeight, x, y, image);
        this.pixels = new float[info.getSpriteWidth() * info.getSpriteHeight()];
        this.baseLayer = new float[info.getSpriteWidth() * info.getSpriteHeight()];
        this.adjustmentLayer = new float[info.getSpriteWidth() * info.getSpriteHeight()];
    }

    @Override
    public boolean hasAnimationMetadata()
    {
        return true;
    }

    private void calculate(int x, int y)
    {
        float pixelsSum = -0.05F;
        int pixelIndex = pixelIndex(x, y);

        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                pixelsSum += pixel(pixels, i + OFFSETS[y & 3], j + OFFSETS[x & 3]);

        this.pixels[pixelIndex] = pixelsSum * 0.1F + average(x, y) * 0.8F;
        this.baseLayer[pixelIndex] += adjustmentLayer[pixelIndex] * 0.01F;
        this.baseLayer[pixelIndex] = Math.max(0, baseLayer[pixelIndex]);

        this.adjustmentLayer[pixelIndex] -= 0.062F;

        if (RANDOM.nextFloat() < 0.0062F)
            this.adjustmentLayer[pixelIndex] = 1.33F;
    }

    private float average(int x, int y)
    {
        return (pixel(baseLayer, x, y) + pixel(baseLayer, x + 1, y) + pixel(baseLayer, x + 1, y + 1) + pixel(baseLayer, x, y + 1)) * 0.25F;
    }

    private void set(int x, int y)
    {
        int color = (int)(MathHelper.clamp(pixel(pixels, x, y) * 2, 0, 1) * 255);
        frames[0].setPixelRGBA(x, y, Color.argb(color, color, color, 255));
    }

    private float pixel(float[] pixels, int x, int y)
    {
        return pixels[pixelIndex(x, y)];
    }

    private int pixelIndex(int x, int y)
    {
        int mask = spriteInfo.getSpriteWidth() - 1;
        return (x & mask) + (y & mask) * spriteInfo.getSpriteWidth();
    }

    private void forEachPixel(IntBiConsumer consumer)
    {
        for (int x = 0; x < spriteInfo.getSpriteWidth(); x++)
            for (int y = 0; y < spriteInfo.getSpriteHeight(); y++)
                consumer.accept(x, y);
    }

    @Override
    public void updateAnimation()
    {
        tickCounter++;
        forEachPixel(this::calculate);
        forEachPixel(this::set);
        uploadMipmaps();
    }

    @Override
    public void close()
    {
        for(int i = 1; i < frames.length; i++)
            frames[i].close();
    }
}
