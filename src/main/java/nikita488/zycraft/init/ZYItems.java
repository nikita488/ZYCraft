package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.ZYItemColors;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.item.*;

import java.util.Map;

public class ZYItems
{
    private static final Registrate REGISTRATE = ZYCraft.registrate().itemGroup(() -> ZYGroups.ITEMS, "ZYCraft Items");

    public static final Map<ZYType, ItemEntry<ZychoriumItem>> ZYCHORIUM = ZYType.buildMap("{type}_zychorium", (type, name) -> REGISTRATE.item(name, properties -> new ZychoriumItem(type, properties))
            .color(() -> () -> ZYItemColors.getZYItemColor(type))
            .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("item/zychorium")))
            .tag(ZYTags.Items.ZYCHORIUM)
            .register());

    public static final ItemEntry<Item> ALUMINIUM = REGISTRATE.item("aluminium", Item::new).register();

    public static final ItemEntry<ColorScannerItem> COLOR_SCANNER = REGISTRATE.item("color_scanner", ColorScannerItem::new)
            .properties(properties -> properties.stacksTo(1))
            .color(() -> ZYItemColors.COLOR_SCANNER)
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .recipe((ctx, provider) ->
            {
                DataIngredient source = DataIngredient.items(ZYBlocks.ZYCHORITE);
                ShapedRecipeBuilder.shaped(ctx.getEntry())
                        .define('#', source)
                        .define('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                        .define('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                        .define('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                        .define('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                        .define('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                        .pattern("RGB")
                        .pattern("###")
                        .pattern("D#L")
                        .unlockedBy("has_" + provider.safeName(source), source.getCritereon(provider))
                        .save(provider, provider.safeId(ctx.getEntry()));
            })
            .register();

    public static final ItemEntry<ScytheItem> SCYTHE = REGISTRATE.item("scythe", ScytheItem::new)
            .properties(properties -> properties.durability(250))
            .model((ctx, provider) -> provider.handheld(ctx::getEntry, provider.modLoc("item/scythe")))
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(ctx.getEntry())
                    .define('#', Tags.Items.RODS_WOODEN)
                    .define('X', ZYBlocks.QUARTZ_CRYSTAL.get())
                    .pattern(" X#")
                    .pattern(" # ")
                    .pattern("#X ")
                    .unlockedBy("has_quartz_crystal", RegistrateRecipeProvider.hasItem(ZYBlocks.QUARTZ_CRYSTAL.get()))
                    .save(provider))
            .register();

    public static final ItemEntry<AluminiumFoilItem> ALUMINIUM_FOIL = REGISTRATE.item("aluminium_foil", AluminiumFoilItem::new)
            .color(() -> ZYItemColors.ALUMINIUM_FOIL)
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(ctx.getEntry())
                    .define('#', ALUMINIUM.get())
                    .define('X', Tags.Items.GLASS)
                    .define('Z', ZYBlocks.ZYCHORITE.get())
                    .define('G', ZYCHORIUM.get(ZYType.GREEN).get())
                    .pattern("#Z#")
                    .pattern("GXG")
                    .pattern("#Z#")
                    .unlockedBy("has_aluminium", RegistrateRecipeProvider.hasItem(ALUMINIUM.get()))
                    .save(provider))
            .register();

    public static final ItemEntry<ZYBucketItem> QUARTZ_BUCKET = REGISTRATE.item("quartz_bucket", ZYBucketItem::new)
            .properties(properties -> properties.stacksTo(16))
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(ctx.getEntry())
                    .define('#', ZYBlocks.QUARTZ_CRYSTAL.get())
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy("has_quartz_crystal", RegistrateRecipeProvider.hasItem(ZYBlocks.QUARTZ_CRYSTAL.get()))
                    .save(provider))
            .register();

    public static final ItemEntry<ZYFluidContainerItem> ALUMINIUM_CAN = REGISTRATE.item("aluminium_can", properties -> new ZYFluidContainerItem(properties, FluidAttributes.BUCKET_VOLUME, 16))
            .model((ctx, provider) -> NonNullBiConsumer.noop())
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 16)
                    .define('#', Tags.Items.GLASS_PANES)
                    .define('X', ALUMINIUM.get())
                    .pattern(" X ")
                    .pattern("X#X")
                    .pattern(" X ")
                    .unlockedBy("has_aluminium", RegistrateRecipeProvider.hasItem(ALUMINIUM.get()))
                    .save(provider))
            .register();

    public static void init() {}
}
