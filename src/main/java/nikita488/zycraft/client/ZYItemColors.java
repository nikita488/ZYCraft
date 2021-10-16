package nikita488.zycraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.client.color.item.ItemColor;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.block.state.properties.ValveIOMode;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.ItemStackUtils;

import java.util.Map;
import java.util.function.Supplier;

public enum ZYItemColors implements Supplier<ItemColor>
{
    ZYCHORIUM_LAMP((stack, tintIndex) -> 0x3F3F3F),
    FABRICATOR((stack, tintIndex) -> ZYType.BLUE.rgb()),
    VALVE((stack, tintIndex) -> tintIndex == 1 ? ValveIOMode.IN.rgb() : ZYType.BLUE.rgb()),
    ITEM_IO((stack, tintIndex) -> tintIndex == 1 ? ItemIOMode.ANY.rgb() : ZYType.GREEN.rgb()),
    COLOR_SCANNER((stack, tintIndex) -> ItemStackUtils.getInt(stack, "Color", 0xFFFFFF)),
    ALUMINIUM_FOIL((stack, tintIndex) -> tintIndex == 1 ? FluidUtil.getFluidContained(stack)
            .map(containedFluid -> containedFluid.getFluid().getAttributes().getColor(containedFluid))
            .orElse(0xFFFFFF) : ZYType.GREEN.rgb());

    public static final Map<ZYType, ItemColor> ZY_COLORS = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, ItemColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (stack, tintIndex) -> type.rgb());

        return builder.build();
    });
    public static final Map<ZYType, ItemColor> ZY_COLORS_WITH_OVERLAY = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, ItemColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (stack, tintIndex) -> tintIndex == 1 ? type.overlayRGB() : type.rgb());

        return builder.build();
    });
    private final ItemColor color;

    ZYItemColors(ItemColor color)
    {
        this.color = color;
    }

    public static ItemColor getZYItemColor(ZYType type)
    {
        return getZYItemColor(type, false);
    }

    public static ItemColor getZYItemColor(ZYType type, boolean coloredOverlay)
    {
        return (coloredOverlay ? ZY_COLORS_WITH_OVERLAY : ZY_COLORS).get(type);
    }

    @Override
    public ItemColor get()
    {
        return color;
    }
}
