package nikita488.zycraft.client.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.GuiComponent;

import java.util.Arrays;
import java.util.stream.Stream;

public class GuiComponentManager extends TextureAtlasHolder
{
    public static final ResourceLocation ATLAS_ID = ZYCraft.id("textures/atlas/gui_components.png");

    public GuiComponentManager(TextureManager manager)
    {
        super(manager, ATLAS_ID, "gui/component");
    }

    public TextureAtlasSprite get(GuiComponent component)
    {
        return getSprite(component.id());
    }

    @Override
    protected Stream<ResourceLocation> getResourcesToLoad()
    {
        return Arrays.stream(GuiComponent.VALUES).map(GuiComponent::id);
    }
}
