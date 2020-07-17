package nikita488.zycraft.init;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ZyType;
import nikita488.zycraft.item.ColorScannerItem;
import nikita488.zycraft.item.ZychoriumItem;
import nikita488.zycraft.util.ModColors;
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
            .recipe((ctx, provider) ->
            {
                DataIngredient source = DataIngredient.items(ModBlocks.ZYCHORITE);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                        .patternLine("RGB")
                        .patternLine("###")
                        .patternLine("D#L")
                        .key('#', source)
                        .key('R', ModItems.ZYCHORIUM.get(ZyType.RED).get())
                        .key('G', ModItems.ZYCHORIUM.get(ZyType.GREEN).get())
                        .key('B', ModItems.ZYCHORIUM.get(ZyType.BLUE).get())
                        .key('D', ModItems.ZYCHORIUM.get(ZyType.DARK).get())
                        .key('L', ModItems.ZYCHORIUM.get(ZyType.LIGHT).get())
                        .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                        .build(provider, provider.safeId(ctx.getEntry()));
            })
            .register();

    public static <T extends Item> ImmutableMap<ZyType, ItemEntry<T>> zyItem(String pattern, NonNullFunction<ZyType, ItemEntry<T>> factory)
    {
        ImmutableMap.Builder<ZyType, ItemEntry<T>> items = ImmutableMap.builder();

        for (ZyType type : ZyType.VALUES)
        {
            REGISTRY.object(pattern.replace("{type}", type.getName()));
            items.put(type, factory.apply(type));
        }

        return items.build();
    }

    public static void init() {}
}
