package com.nikita488.zycraft.init;

import com.google.common.collect.ImmutableMap;
import com.nikita488.zycraft.ZYCraft;
import com.nikita488.zycraft.enums.ZyType;
import com.nikita488.zycraft.item.ColorScannerItem;
import com.nikita488.zycraft.item.ZychoriumItem;
import com.nikita488.zycraft.util.ModColors;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.item.Item;

import java.util.Map;

public class ModItems
{
    private static final Registrate REGISTRY = ZYCraft.REGISTRY.itemGroup(() -> ModGroups.ITEMS, "ZYItems");

    public static final Map<ZyType, ItemEntry<ZychoriumItem>> ZYCHORIUM = zyItem("{type}_zychorium", type ->
            REGISTRY.item(properties -> new ZychoriumItem(type, properties))
                    .color(() -> () -> (stack, tintIndex) -> type.rgb())
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("obj/zychorium")))
                    .tag(ModTags.Items.ZYCHORIUM)
                    .register());

    public static final ItemEntry<Item> ALUMINIUM = REGISTRY.object("aluminium")
            .item(Item::new)
            .register();

    public static final ItemEntry<ColorScannerItem> COLOR_SCANNER = REGISTRY.object("color_scanner")
            .item(ColorScannerItem::new)
            .color(() -> ModColors::colorScannerColor)
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .register();

    public static <T extends Item> ImmutableMap<ZyType, ItemEntry<T>> zyItem(String pattern, NonNullFunction<ZyType, ItemEntry<T>> factory)
    {
        ImmutableMap.Builder<ZyType, ItemEntry<T>> items = ImmutableMap.builder();

        for (ZyType type : ZyType.values())
        {
            REGISTRY.object(pattern.replace("{type}", type.getName()));
            items.put(type, factory.apply(type));
        }

        return items.build();
    }

    public static void init() {}
}
