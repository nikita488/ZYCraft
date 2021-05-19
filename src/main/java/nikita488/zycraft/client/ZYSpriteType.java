package nikita488.zycraft.client;

import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

public enum ZYSpriteType
{
    BACKGROUND("background"),
    MENU_TOP("menu_top"),
    MENU_MIDDLE("menu_middle"),
    MENU_BOTTOM("menu_bottom"),
    MENU_ITEM("menu_item"),
    RIGHT_ARROW("right_arrow");

    private final ResourceLocation name;
    public static final ZYSpriteType[] VALUES = values();

    ZYSpriteType(String name)
    {
        this.name = ZYCraft.modLoc(name);
    }

    public ResourceLocation spriteName()
    {
        return name;
    }
}
