package nikita488.zycraft.enums;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IStringSerializable;

import java.util.Random;

public enum ZYType implements IStringSerializable
{
    BLUE("blue", 0x0064FF, 0x001940, MaterialColor.COLOR_BLUE),
    GREEN("green", 0x00FF00, 0x002000, MaterialColor.COLOR_GREEN),
    RED("red", 0xFF0000, 0x200000, MaterialColor.COLOR_RED),
    DARK("dark", 0x1E1E1E, 0x1E1E1E, MaterialColor.COLOR_BLACK),
    LIGHT("light", 0xFFFFFF, 0xFFFFFF, MaterialColor.QUARTZ);

    private final String name;
    private final int color, darkColor;
    private final MaterialColor mtlColor;
    public static final ZYType[] VALUES = values();

    ZYType(String name, int color, int darkColor, MaterialColor mtlColor)
    {
        this.name = name;
        this.color = color;
        this.darkColor = darkColor;
        this.mtlColor = mtlColor;
    }

    public static ZYType random(Random random)
    {
        return VALUES[random.nextInt(VALUES.length)];
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }

    public int rgb()
    {
        return color;
    }

    public int darkRGB()
    {
        return darkColor;
    }

    public MaterialColor mtlColor()
    {
        return mtlColor;
    }
}
