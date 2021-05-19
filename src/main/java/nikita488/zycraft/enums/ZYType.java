package nikita488.zycraft.enums;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Util;

import java.util.Random;

public enum ZYType implements IStringSerializable
{
    BLUE("blue", 0x0064FF, 0x00193F, MaterialColor.BLUE),
    GREEN("green", 0x00FF00, 0x002000, MaterialColor.GREEN),
    RED("red", 0xFF0000, 0x200000, MaterialColor.RED),
    DARK("dark", 0x1E1E1E, 0x1E1E1E, MaterialColor.BLACK),
    LIGHT("light", 0xFFFFFF, 0xFFFFFF, MaterialColor.QUARTZ);

    private final String name;
    private final int color, overlayColor;
    private final MaterialColor mtlColor;
    public static final ZYType[] VALUES = values();

    ZYType(String name, int color, int overlayColor, MaterialColor mtlColor)
    {
        this.name = name;
        this.color = color;
        this.overlayColor = overlayColor;
        this.mtlColor = mtlColor;
    }

    public static ZYType randomType(Random random)
    {
        return Util.getRandomObject(VALUES, random);
    }

    @Override
    public String getString()
    {
        return name;
    }

    public int rgb()
    {
        return color;
    }

    public int overlayRGB()
    {
        return overlayColor;
    }

    public MaterialColor mtlColor()
    {
        return mtlColor;
    }
}
