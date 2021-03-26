package nikita488.zycraft.util;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZYType;

import java.util.Map;

public class RegistrateUtils
{
    public static <T extends IForgeRegistryEntry<? super T>, E extends RegistryEntry<T>> Map<ZYType, E> zyBase(String pattern, NonNullBiFunction<ZYType, String, E> factory)
    {
        ImmutableMap.Builder<ZYType, E> entries = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            entries.put(type, factory.apply(type, pattern.replace("{type}", type.getSerializedName())));

        return entries.build();
    }

    public static <T extends IForgeRegistryEntry<? super T>, E extends RegistryEntry<T>> Map<ViewerType, E> viewerBase(String pattern, boolean immortal, NonNullBiFunction<ViewerType, String, E> factory)
    {
        ImmutableMap.Builder<ViewerType, E> entries = ImmutableMap.builder();
        ViewerType[] values = immortal ? ViewerType.IMMORTAL_VALUES : ViewerType.VALUES;

        for (ViewerType type : values)
        {
            String target = immortal && type == ViewerType.BASIC ? "{type}_" : "{type}";
            String replacement = immortal && type == ViewerType.BASIC ? "" : type.getSerializedName();
            entries.put(type, factory.apply(type, pattern.replace(target, replacement)));
        }

        return entries.build();
    }
}
