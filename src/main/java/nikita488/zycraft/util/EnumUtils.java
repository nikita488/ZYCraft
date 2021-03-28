package nikita488.zycraft.util;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZYType;

import java.util.Map;

public class EnumUtils
{
    public static <T> Map<ZYType, T> zyBase(String pattern, NonNullBiFunction<ZYType, String, T> factory)
    {
        ImmutableMap.Builder<ZYType, T> entries = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            entries.put(type, factory.apply(type, pattern.replace("{type}", type.getString())));

        return entries.build();
    }

    public static <T> Map<ViewerType, T> viewerBase(String pattern, boolean immortal, NonNullBiFunction<ViewerType, String, T> factory)
    {
        ImmutableMap.Builder<ViewerType, T> entries = ImmutableMap.builder();
        ViewerType[] values = immortal ? ViewerType.IMMORTAL_VALUES : ViewerType.VALUES;

        for (ViewerType type : values)
        {
            String target = immortal && type == ViewerType.BASIC ? "{type}_" : "{type}";
            String replacement = immortal && type == ViewerType.BASIC ? "" : type.getString();
            entries.put(type, factory.apply(type, pattern.replace(target, replacement)));
        }

        return entries.build();
    }
}
