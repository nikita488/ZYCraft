package nikita488.zycraft.init;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.data.ShapedRecipeBuilder;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.ZYColors;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.item.ColorScannerItem;
import nikita488.zycraft.item.ZychoriumItem;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.item.Item;

import java.util.Map;

public class ZYItems
{
    private static final Registrate REGISTRY = ZYCraft.REGISTRY.itemGroup(() -> ZYGroups.ITEMS, "ZYCraft Items");

    public static final Map<ZYType, ItemEntry<ZychoriumItem>> ZYCHORIUM = zyItem("{type}_zychorium", type ->
            REGISTRY.item(properties -> new ZychoriumItem(type, properties))
                    .color(() -> () -> (stack, tintIndex) -> type.rgb())
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("item/zychorium")))
                    .tag(ZYTags.Items.ZYCHORIUM)
                    .register());

    public static final ItemEntry<Item> ALUMINIUM = REGISTRY.object("aluminium")
            .item(Item::new)
            .register();

    public static final ItemEntry<ColorScannerItem> COLOR_SCANNER = REGISTRY.object("color_scanner")
            .item(ColorScannerItem::new)
            .color(() -> ZYColors::colorScannerColor)
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .recipe((ctx, provider) ->
            {
                DataIngredient source = DataIngredient.items(ZYBlocks.ZYCHORITE);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .patternLine("RGB")
                        .patternLine("###")
                        .patternLine("D#L")
                        .key('#', source)
                        .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                        .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                        .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                        .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                        .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                        .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                        .build(provider, provider.safeId(ctx.getEntry()));
            })
            .register();

    public static <T extends Item> ImmutableMap<ZYType, ItemEntry<T>> zyItem(String pattern, NonNullFunction<ZYType, ItemEntry<T>> factory)
    {
        ImmutableMap.Builder<ZYType, ItemEntry<T>> items = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
        {
            REGISTRY.object(pattern.replace("{type}", type.getName()));
            items.put(type, factory.apply(type));
        }

        return items.build();
    }

    public static void init() {}
}
