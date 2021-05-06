package nikita488.zycraft.client.resources;

import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Arrays;
import java.util.stream.Stream;

public class ZYWidgetTextureManager extends SpriteUploader
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("textures/atlas/widgets.png");

    public ZYWidgetTextureManager(TextureManager manager)
    {
        super(manager, NAME, "gui/widget");
    }

    public TextureAtlasSprite get(ZYWidget widget)
    {
        return getSprite(widget.textureName());
    }

    @Override
    protected Stream<ResourceLocation> getResourceLocations()
    {
        return Arrays.stream(ZYWidget.VALUES).map(ZYWidget::textureName);
    }
}
