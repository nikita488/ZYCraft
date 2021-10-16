package nikita488.zycraft.client.gui;

import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;

public enum GuiComponent
{
    BACKGROUND("background", 128, 128),
    MENU_TOP("menu_top", 34, 5),
    MENU_MIDDLE("menu_middle", 34, 24),
    MENU_BOTTOM("menu_bottom", 34, 3),
    MENU_ITEM("menu_item", 22, 22),
    RIGHT_ARROW("right_arrow", 80, 32),
    BIG_TANK("big_tank", 54, 60),
    BIG_TANK_BACKGROUND("big_tank_background", 216, 240),
    BIG_TANK_GAUGE("big_tank_gauge", 216, 240),
    DROP("drop", 64, 64);

    private final ResourceLocation id;
    private final int width, height;
    public static final GuiComponent[] VALUES = values();

    GuiComponent(String name, int width, int height)
    {
        this.id = ZYCraft.id(name);
        this.width = width;
        this.height = height;
    }

    public ResourceLocation id()
    {
        return id;
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    @Override
    public String toString()
    {
        return id.toString();
    }
}
