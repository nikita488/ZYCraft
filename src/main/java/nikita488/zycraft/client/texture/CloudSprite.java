package nikita488.zycraft.client.texture;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.textures.ITextureAtlasSpriteLoader;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.util.Color;
import nikita488.zycraft.util.IntBiConsumer;

import javax.annotation.Nonnull;
import java.util.Random;

public class CloudSprite extends TextureAtlasSprite
{
    public static final RenderMaterial MATERIAL = ModelLoaderRegistry.blockMaterial(ZYCraft.id("cloud"));
    private static final int[] OFFSETS = new int[] {0, -1, 0, 1};
    private static final Random RANDOM = new Random();
    private final float[] pixels, baseLayer, adjustmentLayer;

    protected CloudSprite(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipMapLevel, int atlasWidth, int atlasHeight, int x, int y, NativeImage image)
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
        int color = (int)(MathHelper.clamp(pixel(pixels, x, y) * 2F, 0F, 1F) * 255);
        mainImage[0].setPixelRGBA(x, y, Color.argb(color, color, color, 255));
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
        for (int i = 1; i < mainImage.length; i++)
            mainImage[i].close();
    }

    public static class Loader implements ITextureAtlasSpriteLoader
    {
        @Nonnull
        @Override
        public TextureAtlasSprite load(AtlasTexture atlas, IResourceManager manager, Info info, IResource resource, int atlasWidth, int atlasHeight, int spriteX, int spriteY, int mipMapLevel, NativeImage image)
        {
            return new CloudSprite(atlas, info, mipMapLevel, atlasWidth, atlasHeight, spriteX, spriteY, Util.make(new NativeImage(image.getWidth(), image.getHeight(), false), NativeImage::untrack));
        }
    }
}
