package nikita488.zycraft.client.texture;

import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Arrays;
import java.util.stream.Stream;

public class ZYSpriteTextureManager extends SpriteUploader
{
    public static final ResourceLocation ATLAS_ID = ZYCraft.id("textures/atlas/sprites.png");

    public ZYSpriteTextureManager(TextureManager manager)
    {
        super(manager, ATLAS_ID, "gui/sprite");
    }

    public TextureAtlasSprite get(ZYSpriteType type)
    {
        return getSprite(type.spriteName());
    }

    @Override
    protected Stream<ResourceLocation> getResourceLocations()
    {
        return Arrays.stream(ZYSpriteType.VALUES).map(ZYSpriteType::spriteName);
    }
}
