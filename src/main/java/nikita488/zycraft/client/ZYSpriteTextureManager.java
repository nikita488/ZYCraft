package nikita488.zycraft.client;

import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Arrays;
import java.util.stream.Stream;

public class ZYSpriteTextureManager extends SpriteUploader
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("textures/atlas/sprites.png");

    public ZYSpriteTextureManager(TextureManager manager)
    {
        super(manager, NAME, "gui/sprite");
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
