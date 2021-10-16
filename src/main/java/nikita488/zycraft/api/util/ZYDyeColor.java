package nikita488.zycraft.api.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public enum ZYDyeColor
{
    WHITE(0xFFFFFF),
    ORANGE(0xFF8200),
    MAGENTA(0xFF00FF),
    LIGHT_BLUE(0x69C3FF),
    YELLOW(0xFFFF00),
    LIME(0x00FF00),
    PINK(0xFF87FF),
    GRAY(0x808080),
    LIGHT_GRAY(0xC0C0C0),
    CYAN(0x00FFFF),
    PURPLE(0x9600FA),
    BLUE(0x0000FF),
    BROWN(0x6E4100),
    GREEN(0x008000),
    RED(0xFF0000),
    BLACK(0x2C2C2C);

    public static final ZYDyeColor[] VALUES = values();
    private final int color;

    ZYDyeColor(int color)
    {
        this.color = color;
    }

    public int rgb()
    {
        return color;
    }

    @Nullable
    public static ZYDyeColor byDyeColor(ItemStack stack)
    {
        DyeColor color = DyeColor.getColor(stack);
        return color != null ? byDyeColor(color) : null;
    }

    public static ZYDyeColor byDyeColor(DyeColor color)
    {
        return VALUES[color.getId()];
    }
}
