package nikita488.zycraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.stream.Stream;

public class WidgetTextureAtlas extends SpriteUploader
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("textures/atlas/widgets.png");

    public WidgetTextureAtlas()
    {
        super(Minecraft.getInstance().getTextureManager(), NAME, "gui/widget");
    }

    @Override
    public TextureAtlasSprite getSprite(ResourceLocation name)
    {
        return super.getSprite(name);
    }

    public TextureAtlasSprite getSprite(ZYWidget widget)
    {
        return getSprite(widget.textureName());
    }

    @Override
    protected Stream<ResourceLocation> getResourceLocations()
    {
        return ZYWidget.textures();
    }
}
