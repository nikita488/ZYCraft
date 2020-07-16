package nikita488.zycraft.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.*;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZyType;
import nikita488.zycraft.util.ModColors;

import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final Registrate REGISTRY = ZYCraft.REGISTRY.itemGroup(() -> ModGroups.BLOCKS, "ZYBlocks");

    public static final BlockEntry<Block> ZYCHORITE = REGISTRY.object("zychorite")
            .block(Block::new)
            .properties(properties -> properties.hardnessAndResistance(1.5F, 6))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BLOCK = REGISTRY.object("zychorite_block")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, ZYCHORITE, ctx::getEntry))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BRICKS = REGISTRY.object("zychorite_bricks")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, DataIngredient.items(ZYCHORITE), ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ZYCHORITE_BRICKS = REGISTRY.object("small_zychorite_bricks")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, ZYCHORITE_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_ORE = REGISTRY.object("aluminium_ore")
            .block(Block::new)
            .properties(properties -> properties.hardnessAndResistance(3, 3))
            .simpleItem()
            .recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.items(ctx.getEntry()), ModItems.ALUMINIUM, 0.1F))
            .tag(ModTags.Blocks.ORES_ALUMINIUM, ModTags.Blocks.ORES_ALUMINUM)
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BLOCK = REGISTRY.object("aluminium_block")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, ModItems.ALUMINIUM, ctx::getEntry))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BRICKS = REGISTRY.object("aluminium_bricks")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ModItems.ALUMINIUM), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ALUMINIUM_BRICKS = REGISTRY.object("small_aluminium_bricks")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, ALUMINIUM_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalBlock> QUARTZ_CRYSTAL = REGISTRY.object("quartz_crystal")
            .block(QuartzCrystalBlock::new)
            .initialProperties(Material.MISCELLANEOUS, Material.MISCELLANEOUS.getColor())
            .properties(properties -> properties.hardnessAndResistance(0.3F).lightValue(9).sound(SoundType.GLASS).notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .acceptCondition(SurvivesExplosion.builder())
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(block)
                                    .acceptFunction(SetCount.builder(ConstantRange.of(2))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalBlock.AMOUNT, 2))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(3))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalBlock.AMOUNT, 3))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(4))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalBlock.AMOUNT, 4))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(5))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalBlock.AMOUNT, 5))))))))
            .blockstate((ctx, provider) -> NonNullConsumer.noop())
            .item()
                .model((ctx, provider) -> provider.blockItem(ctx::getEntry, "_1"))
                .build()
            .register();

    public static final BlockEntry<QuartzCrystalDecoBlock> QUARTZ_CRYSTAL_BLOCK = REGISTRY.object("quartz_crystal_block")
            .block(QuartzCrystalDecoBlock::new)
            .initialProperties(Material.GLASS, Material.GLASS.getColor())
            .properties(properties -> properties.hardnessAndResistance(0.3F).lightValue(9).sound(SoundType.GLASS).notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, QUARTZ_CRYSTAL, ctx::getEntry))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .register();

    public static final BlockEntry<QuartzCrystalDecoBlock> QUARTZ_CRYSTAL_BRICKS = REGISTRY.object("quartz_crystal_bricks")
            .block(QuartzCrystalDecoBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .simpleItem()
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(QUARTZ_CRYSTAL), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalDecoBlock> SMALL_QUARTZ_CRYSTAL_BRICKS = REGISTRY.object("small_quartz_crystal_bricks")
            .block(QuartzCrystalDecoBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .simpleItem()
            .recipe((ctx, provider) -> smallBricks(provider, QUARTZ_CRYSTAL_BRICKS, ctx::getEntry))
            .register();

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIUM_ORE = zyBlock("{type}_zychorium_ore", (type, builder) ->
            builder.initialProperties(ALUMINIUM_ORE)
                    .tag(ModTags.Blocks.ORES_ZYCHORIUM)
                    .loot((tables, block) -> tables.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouch(block,
                            ItemLootEntry.builder(ModItems.ZYCHORIUM.get(type).get())
                                    .acceptFunction(ExplosionDecay.builder())
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))
                                    .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))))));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIUM_BLOCK = zyBlock("{type}_zychorium_block", (type, builder) ->
            builder.tag(ModTags.Blocks.STORAGE_BLOCKS_ZYCHORIUM)
                    .recipe((ctx, provider) -> storageBlock(provider, ModItems.ZYCHORIUM.get(type), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIUM_BRICKS = zyBricks("{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.BRICKS_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(ModItems.ZYCHORIUM.get(type)), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> SMALL_ZYCHORIUM_BRICKS = zyBricks("small_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.SMALL_BRICKS_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<Block>> SOLID_ZYCHORIUM_BRICKS = solidZyBricks("solid_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.BRICKS_SOLID_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(Blocks.STONE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<Block>> SMALL_SOLID_ZYCHORIUM_BRICKS = solidZyBricks("small_solid_{type}_zychorium_bricks", (color, builder) ->
            builder.tag(ModTags.Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, SOLID_ZYCHORIUM_BRICKS.get(color), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("zychorized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> SMALL_ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("small_zychorized_{type}_zychorium_bricks", (color, builder) ->
            builder.tag(ModTags.Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIZED_ZYCHORIUM_BRICKS.get(color), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("aluminized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.BRICKS_ALUMINIZED_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ModItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> SMALL_ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("small_aluminized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ModTags.Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ALUMINIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIUM_PLATE = zyBlock("{type}_zychorium_plate", (type, builder) ->
            builder.properties(properties -> properties.hardnessAndResistance(1.5F, 12))
                    .tag(ModTags.Blocks.ZYCHORIUM_PLATE)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.tag(Tags.Items.INGOTS_IRON), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIUM_SHIELD = zyBlock("{type}_zychorium_shield", (type, builder) ->
            builder.properties(properties -> properties.hardnessAndResistance(1.5F, 1200))
                    .tag(ModTags.Blocks.ZYCHORIUM_SHIELD)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.tag(Tags.Items.OBSIDIAN), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<ZychoriumLampBlock> ZYCHORIUM_LAMP = lamp(false);

    public static final BlockEntry<ZychoriumLampBlock> INVERTED_ZYCHORIUM_LAMP = lamp( true);

    public static final BlockEntry<ColorableBlock> IMMORTAL_BLOCK = REGISTRY.object("immortal_block")
            .block(ColorableBlock::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ModColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/zy_cube_all"))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.tag(ModTags.Items.STORAGE_BLOCKS_ZYCHORIUM), ctx::getEntry))
            .tag(ModTags.Blocks.COLORABLE)
            .simpleItem()
            .register();

    public static final BlockEntry<ColorableBlock> THE_AUREY_BLOCK = REGISTRY.object("the_aurey_block")
            .block(ColorableBlock::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ModColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/colorable_cube_all"))
                    .texture("base", provider.modLoc("block/" + ctx.getName()))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.items(ALUMINIUM_BLOCK), ctx::getEntry))
            .tag(ModTags.Blocks.COLORABLE)
            .simpleItem()
            .register();

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> VIEWER = viewer(false);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> IMMORTAL_VIEWER = immortalViewer(false);

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> PHANTOMIZED_VIEWER = viewer(true);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> PHANTOMIZED_IMMORTAL_VIEWER = immortalViewer(true);

    public static final Map<ZyType, BlockEntry<ZyBlock>> ZYCHORIZED_ENGINEERING_BLOCK = zyBlock("zychorized_{type}_engineering_block", (type, builder) ->
            builder.addLayer(() -> RenderType::getTranslucent)
                    .tag(ModTags.Blocks.ZYCHORIZED_ENGINEERING_BLOCK)
                    .recipe((ctx, provider) ->
                            engineering(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZyType, BlockEntry<ZyBlock>> ALUMINIZED_ENGINEERING_BLOCK = zyBlock("aluminized_{type}_engineering_block", (type, builder) ->
            builder.addLayer(() -> RenderType::getTranslucent)
                    .tag(ModTags.Blocks.ALUMINIZED_ENGINEERING_BLOCK)
                    .recipe((ctx, provider) ->
                            engineering(provider, DataIngredient.items(ModItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    private static ImmutableMap<ZyType, BlockEntry<ZyBlock>> zyBlock(String pattern, NonNullBiFunction<ZyType, BlockBuilder<ZyBlock, Registrate>, BlockBuilder<ZyBlock, Registrate>> factory)
    {
        return zyBase(pattern, false, factory);
    }

    private static ImmutableMap<ZyType, BlockEntry<ZyBlock>> zyBricks(String pattern, NonNullBiFunction<ZyType, BlockBuilder<ZyBlock, Registrate>, BlockBuilder<ZyBlock, Registrate>> factory)
    {
        return zyBase(pattern, true, factory);
    }

    private static ImmutableMap<ZyType, BlockEntry<ZyBlock>> zyBase(String pattern, boolean bricks, NonNullBiFunction<ZyType, BlockBuilder<ZyBlock, Registrate>, BlockBuilder<ZyBlock, Registrate>> factory)
    {
        ImmutableMap.Builder<ZyType, BlockEntry<ZyBlock>> blocks = ImmutableMap.builder();
        String name = pattern.replace("{type}_", "");

        for (ZyType type : ZyType.VALUES)
        {
            blocks.put(type, factory.apply(type, REGISTRY.object(pattern.replace("{type}", type.getName()))
                    .block(properties -> new ZyBlock(type, properties))
                    .initialProperties(ZYCHORITE)
                    .addLayer(() -> RenderType::getCutout)
                    .color(() -> () -> ModColors.zyBlockColor(type, bricks))
                    .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                            .withExistingParent(name, provider.modLoc("block/" + (bricks ? "zy_bricks" : "zy_cube_all")))
                            .texture("all", provider.modLoc("block/" + name)))))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .color(() -> () -> ModColors.zyItemColor(type, bricks))
                        .build()
                    .register());
        }

        return blocks.build();
    }

    private static ImmutableMap<ZyType, BlockEntry<Block>> solidZyBricks(String pattern, NonNullBiFunction<ZyType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        ImmutableMap.Builder<ZyType, BlockEntry<Block>> blocks = ImmutableMap.builder();

        for (ZyType type : ZyType.VALUES)
        {
            blocks.put(type, factory.apply(type, REGISTRY.object(pattern.replace("{type}", type.getName()))
                    .block(Block::new)
                    .simpleItem())
                    .register());
        }

        return blocks.build();
    }

    private static BlockEntry<ZychoriumLampBlock> lamp(boolean inverted)
    {
        String name = "zychorium_lamp";
        return REGISTRY.object(inverted ? "inverted_" + name : name)
                .block(Material.REDSTONE_LIGHT, properties -> new ZychoriumLampBlock(inverted, properties))
                .properties(properties -> properties.hardnessAndResistance(0.3F, 6.0F).lightValue(15).sound(SoundType.GLASS))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> ModColors::colorableBlockColor)
                .blockstate((ctx, provider) ->
                {
                    if (!inverted)
                        provider.simpleBlock(ctx.getEntry(), provider.models()
                                .withExistingParent(ctx.getName(), provider.modLoc("block/zy_cube_all"))
                                .texture("all", provider.modLoc("block/" + ctx.getName())));
                    else
                        provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));
                })
                .recipe((ctx, provider) ->
                {
                    DataIngredient source = DataIngredient.items(Blocks.GLOWSTONE);

                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                            .patternLine("RGB")
                            .patternLine("PSP")
                            .patternLine("D#L")
                            .key('S', source)
                            .key('P', Tags.Items.DUSTS_REDSTONE)
                            .key('#', inverted ? Ingredient.fromItems(Items.REDSTONE_TORCH) : Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE))
                            .key('R', ModItems.ZYCHORIUM.get(ZyType.RED).get())
                            .key('G', ModItems.ZYCHORIUM.get(ZyType.GREEN).get())
                            .key('B', ModItems.ZYCHORIUM.get(ZyType.BLUE).get())
                            .key('D', ModItems.ZYCHORIUM.get(ZyType.DARK).get())
                            .key('L', ModItems.ZYCHORIUM.get(ZyType.LIGHT).get())
                            .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));
                })
                .tag(ModTags.Blocks.ZYCHORIUM_LAMPS)
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                    .color(() -> () -> ModColors.zyLampItemColor(inverted))
                    .build()
                .register();
    }

    private static ImmutableMap<ViewerType, BlockEntry<ViewerBlock>> viewer(boolean phantomized)
    {
        ImmutableMap.Builder<ViewerType, BlockEntry<ViewerBlock>> blocks = ImmutableMap.builder();

        for (ViewerType type : ViewerType.VALUES)
        {
            String name = type.getName() + "_viewer";

            blocks.put(type, REGISTRY.object(phantomized ? "phantomized_" + name : name)
                    .block(properties -> new ViewerBlock(type, properties))
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(properties -> type.properties(properties, phantomized))
                    .addLayer(() -> type::layer)
                    .blockstate((ctx, provider) ->
                    {
                        if (!phantomized)
                            provider.simpleBlock(ctx.getEntry());
                        else
                            provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));
                    })
                    .tag(type.tag(phantomized))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .build()
                    .recipe((ctx, provider) ->
                    {
                        if (!phantomized)
                            infused(provider, type.ingredient(), DataIngredient.tag(Tags.Items.GLASS), ctx::getEntry);
                        else
                            infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(VIEWER.get(type)), ctx::getEntry);
                    })
                    .register());
        }

        return blocks.build();
    }

    private static ImmutableMap<ViewerType, BlockEntry<ImmortalViewerBlock>> immortalViewer(boolean phantomized)
    {
        ImmutableMap.Builder<ViewerType, BlockEntry<ImmortalViewerBlock>> blocks = ImmutableMap.builder();
        String name = "immortal_viewer";

        for (ViewerType type : ViewerType.IMMORTAL_VALUES)
        {
            String registryName = type.getName() + "_viewer";

            blocks.put(type, REGISTRY.object(phantomized ? "phantomized_" + registryName : registryName)
                    .block(properties -> new ImmortalViewerBlock(type, properties))
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(properties -> type.properties(properties, phantomized))
                    .addLayer(() -> type::layer)
                    .color(() -> ModColors::colorableBlockColor)
                    .blockstate((ctx, provider) ->
                    {
                        if (!phantomized && type == ViewerType.IMMORTAL)
                            provider.simpleBlock(ctx.getEntry(), provider.models()
                                    .withExistingParent(name, provider.modLoc("block/colorable_cube_all"))
                                    .texture("base", provider.modLoc("block/immortal_viewer_base"))
                                    .texture("all", provider.modLoc("block/immortal_viewer_overlay")));
                        else
                            provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc(name)));

                    })
                    .tag(type.tag(phantomized))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .build()
                    .recipe((ctx, provider) ->
                    {
                        if (!phantomized)
                            if (type == ViewerType.IMMORTAL)
                                colorable(provider, DataIngredient.tag(Tags.Items.GLASS), ctx::getEntry);
                            else
                                infused(provider, type.ingredient(), DataIngredient.items(IMMORTAL_VIEWER.get(ViewerType.IMMORTAL)), ctx::getEntry);
                        else
                            infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(IMMORTAL_VIEWER.get(type)), ctx::getEntry);
                    })
                    .register());
        }

        return blocks.build();
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void storageBlock(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> source, NonNullSupplier<? extends T> result)
    {
        storageBlock(provider, DataIngredient.items(source), result, source);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void storageBlock(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result, NonNullSupplier<? extends T> reverseSource)
    {
        provider.square(source, result, false);
        provider.singleItemUnfinished(DataIngredient.items(result), reverseSource, 1, 9)
                .build(provider, provider.safeId(source) + "_from_" + provider.safeName(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void bricks(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        provider.square(source, result, true);
        provider.stonecutting(source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void smallBricks(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> source, NonNullSupplier<? extends T> result)
    {
        smallBricks(provider, DataIngredient.items(source), result, source);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void smallBricks(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result, NonNullSupplier<? extends T> reverseSource)
    {
        provider.square(source, result, true);
        ShapedRecipeBuilder.shapedRecipe(reverseSource.get())
                .patternLine("XX")
                .patternLine("XX")
                .key('X', result.get())
                .addCriterion("has_" + provider.safeName(result.get()), provider.hasItem(result.get()))
                .build(provider, provider.safeId(source) + "_from_" + provider.safeName(result.get()));
        provider.stonecutting(source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void infused(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, Supplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .patternLine("I#I")
                .patternLine("# #")
                .patternLine("I#I")
                .key('I', infusionSource)
                .key('#', source)
                .addCriterion("has_" + provider.safeName(infusionSource), infusionSource.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void engineering(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, Supplier<? extends T> result)
    {
        DataIngredient redstoneIngredient = DataIngredient.tag(Tags.Items.DUSTS_REDSTONE);

        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .patternLine("I#I")
                .patternLine("#R#")
                .patternLine("I#I")
                .key('I', infusionSource)
                .key('#', source)
                .key('R', redstoneIngredient)
                .addCriterion("has_" + provider.safeName(redstoneIngredient), redstoneIngredient.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void colorable(RegistrateRecipeProvider provider, DataIngredient source, Supplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .patternLine("#L#")
                .patternLine("RGB")
                .patternLine("#D#")
                .key('#', source)
                .key('R', ModItems.ZYCHORIUM.get(ZyType.RED).get())
                .key('G', ModItems.ZYCHORIUM.get(ZyType.GREEN).get())
                .key('B', ModItems.ZYCHORIUM.get(ZyType.BLUE).get())
                .key('D', ModItems.ZYCHORIUM.get(ZyType.DARK).get())
                .key('L', ModItems.ZYCHORIUM.get(ZyType.LIGHT).get())
                .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    public static void init() {}
}
