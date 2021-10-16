package nikita488.zycraft.enums;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraft.Util;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Map;
import java.util.Random;

public enum ZYType
{
    BLUE("blue", 0x0064FF, 0x00193F, MaterialColor.COLOR_BLUE),
    GREEN("green", 0x00FF00, 0x002000, MaterialColor.COLOR_GREEN),
    RED("red", 0xFF0000, 0x200000, MaterialColor.COLOR_RED),
    DARK("dark", 0x1E1E1E, 0x1E1E1E, MaterialColor.COLOR_BLACK),
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
        return Util.getRandom(VALUES, random);
    }

    public static <T> Map<ZYType, T> buildMap(String pattern, NonNullBiFunction<ZYType, String, T> factory)
    {
        ImmutableMap.Builder<ZYType, T> map = ImmutableMap.builder();

        for (ZYType type : VALUES)
            map.put(type, factory.apply(type, pattern.replace("{type}", type.toString())));

        return map.build();
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

    @Override
    public String toString()
    {
        return name;
    }
}
