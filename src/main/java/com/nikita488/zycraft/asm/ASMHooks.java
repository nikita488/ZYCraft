package com.nikita488.zycraft.asm;

import com.nikita488.zycraft.client.texture.CloudSprite;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ASMHooks
{
    public static void addCloudSpriteInfo(AtlasTexture atlas, Stitcher stitcher)
    {
        if (atlas.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE))
            stitcher.addSprite(CloudSprite.info());
    }

    public static TextureAtlasSprite createCloudSprite(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipMapLevels, int atlasWidth, int atlasHeight, int x, int y)
    {
        return new CloudSprite(atlas, info, mipMapLevels, atlasWidth, atlasHeight, x, y);
    }
}
