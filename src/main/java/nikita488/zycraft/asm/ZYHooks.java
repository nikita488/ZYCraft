package nikita488.zycraft.asm;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import nikita488.zycraft.client.texture.CloudSprite;
import nikita488.zycraft.config.ZYConfig;

public class ZYHooks
{
    public static void addCloudSpriteInfo(AtlasTexture atlas, Stitcher stitcher)
    {
        if (atlas.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
            stitcher.addSprite(new TextureAtlasSprite.Info(CloudSprite.NAME, ZYConfig.animationSize, ZYConfig.animationSize, AnimationMetadataSection.EMPTY));
    }

    public static TextureAtlasSprite createCloudSprite(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipMapLevels, int atlasWidth, int atlasHeight, int x, int y)
    {
        return new CloudSprite(atlas, info, mipMapLevels, atlasWidth, atlasHeight, x, y);
    }
}
