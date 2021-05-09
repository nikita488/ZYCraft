package nikita488.zycraft.client.resources;

import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Locale;

public enum ZYSpriteType
{
    BACKGROUND,
    MENU_TOP,
    MENU_MIDDLE,
    MENU_BOTTOM,
    MENU_ITEM;

    private final ResourceLocation name;
    public static final ZYSpriteType[] VALUES = values();

    ZYSpriteType()
    {
        this.name = ZYCraft.modLoc(name().toLowerCase(Locale.ROOT));
    }

    public ResourceLocation spriteName()
    {
        return name;
    }
}
