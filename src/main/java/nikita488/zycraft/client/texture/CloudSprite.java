package nikita488.zycraft.client.texture;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.util.Color4b;
import nikita488.zycraft.util.IntBiConsumer;

import java.util.Random;

public class CloudSprite extends TextureAtlasSprite
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("cloud");
    public static final ResourceLocation NAME2 = ZYCraft.modLoc("cloud2");
    private static final Random RANDOM = new Random();
    private final float[] pixels, baseLayer, adjustmentLayer;
    private final int[] offsets = new int[] {0, -1, 0, 1};

    public CloudSprite(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipMapLevels, int atlasWidth, int atlasHeight, int x, int y, NativeImage image)
    {
        super(atlas, info, 0, atlasWidth, atlasHeight, x, y, image);
        this.pixels = new float[info.width() * info.height()];
        this.baseLayer = new float[info.width() * info.height()];
        this.adjustmentLayer = new float[info.width() * info.height()];
    }

    @Override
    public boolean isAnimation()
    {
        return true;
    }

    private void calculate(int x, int y)
    {
        float pixelsSum = -0.05F;
        int pixelIndex = pixelIndex(x, y);

        for (int i = x - 1; i <= x + 1; i++)
            for (int j = y - 1; j <= y + 1; j++)
                pixelsSum += pixel(pixels, i + offsets[y & 3], j + offsets[x & 3]);

        pixels[pixelIndex] = pixelsSum * 0.1F + average(x, y) * 0.8F;
        baseLayer[pixelIndex] += adjustmentLayer[pixelIndex] * 0.01F;
        baseLayer[pixelIndex] = Math.max(0, baseLayer[pixelIndex]);

        adjustmentLayer[pixelIndex] -= 0.062F;

        if (RANDOM.nextFloat() < 0.0062F)
            adjustmentLayer[pixelIndex] = 1.33F;
    }

    private float average(int x, int y)
    {
        return (pixel(baseLayer, x, y) + pixel(baseLayer, x + 1, y) + pixel(baseLayer, x + 1, y + 1) + pixel(baseLayer, x, y + 1)) * 0.25F;
    }

    private void set(int x, int y)
    {
        int color = (int)(MathHelper.clamp(pixel(pixels, x, y) * 2, 0, 1) * 255);
        mainImage[0].setPixelRGBA(x, y, Color4b.rgba(255, color, color, color));
    }

    private float pixel(float[] pixels, int x, int y)
    {
        return pixels[pixelIndex(x, y)];
    }

    private int pixelIndex(int x, int y)
    {
        int mask = info.width() - 1;
        return (x & mask) + (y & mask) * info.width();
    }

    private void forEachPixel(IntBiConsumer consumer)
    {
        for (int x = 0; x < info.width(); x++)
            for (int y = 0; y < info.height(); y++)
                consumer.accept(x, y);
    }

    @Override
    public void cycleFrames()
    {
        subFrame++;
        forEachPixel(this::calculate);
        forEachPixel(this::set);
        uploadFirstFrame();
    }

    @Override
    public void close()
    {
        for(int i = 1; i < mainImage.length; i++)
            mainImage[i].close();
    }
}
