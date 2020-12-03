package nikita488.zycraft.client.gui;

import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Arrays;
import java.util.stream.Stream;

public enum ZYWidget
{
    BACKGROUND("background"),
    SLOT("slot"),
    TANK_BIG("tank_big");

    private final ResourceLocation name;
    public static final ZYWidget[] VALUES = values();

    ZYWidget(String name)
    {
        this.name = ZYCraft.modLoc(name);
    }

    public ResourceLocation textureName()
    {
        return name;
    }

    public static Stream<ResourceLocation> textures()
    {
        return Arrays.stream(VALUES).map(ZYWidget::textureName);
    }
}
