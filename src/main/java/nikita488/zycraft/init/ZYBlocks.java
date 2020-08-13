package nikita488.zycraft.init;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
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
import nikita488.zycraft.client.ZYColors;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.DefaultMultiChildBlock;
import nikita488.zycraft.api.multiblock.child.MultiChildMaterial;
import nikita488.zycraft.multiblock.test.TestBlock;
import nikita488.zycraft.multiblock.test.TestTile;

import java.util.Map;
import java.util.function.Supplier;

public class ZYBlocks
{
    public static final Registrate REGISTRY = ZYCraft.REGISTRY.itemGroup(() -> ZYGroups.BLOCKS, "ZYBlocks");

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
            .recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.items(ctx.getEntry()), ZYItems.ALUMINIUM, 0.1F))
            .tag(ZYTags.Blocks.ORES_ALUMINIUM, ZYTags.Blocks.ORES_ALUMINUM)
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BLOCK = REGISTRY.object("aluminium_block")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> storageBlock(provider, ZYItems.ALUMINIUM, ctx::getEntry))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BRICKS = REGISTRY.object("aluminium_bricks")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
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

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIUM_ORE = zyBlock("{type}_zychorium_ore", (type, builder) ->
            builder.initialProperties(ALUMINIUM_ORE)
                    .tag(ZYTags.Blocks.ORES_ZYCHORIUM)
                    .loot((tables, block) -> tables.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouch(block,
                            ItemLootEntry.builder(ZYItems.ZYCHORIUM.get(type).get())
                                    .acceptFunction(ExplosionDecay.builder())
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))
                                    .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))))));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIUM_BLOCK = zyBlock("{type}_zychorium_block", (type, builder) ->
            builder.tag(ZYTags.Blocks.STORAGE_BLOCKS_ZYCHORIUM)
                    .recipe((ctx, provider) -> storageBlock(provider, ZYItems.ZYCHORIUM.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIUM_BRICKS = zyBricks("{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.BRICKS_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(ZYItems.ZYCHORIUM.get(type)), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> SMALL_ZYCHORIUM_BRICKS = zyBricks("small_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SOLID_ZYCHORIUM_BRICKS = solidZyBricks("solid_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.BRICKS_SOLID_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(Blocks.STONE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_SOLID_ZYCHORIUM_BRICKS = solidZyBricks("small_solid_{type}_zychorium_bricks", (color, builder) ->
            builder.tag(ZYTags.Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, SOLID_ZYCHORIUM_BRICKS.get(color), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("zychorized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> SMALL_ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("small_zychorized_{type}_zychorium_bricks", (color, builder) ->
            builder.tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ZYCHORIZED_ZYCHORIUM_BRICKS.get(color), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("aluminized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.BRICKS_ALUMINIZED_ZYCHORIUM)
                    .recipe((ctx, provider) ->
                    infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> SMALL_ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("small_aluminized_{type}_zychorium_bricks", (type, builder) ->
            builder.tag(ZYTags.Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM)
                    .recipe((ctx, provider) -> smallBricks(provider, ALUMINIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIUM_PLATE = zyBlock("{type}_zychorium_plate", (type, builder) ->
            builder.properties(properties -> properties.hardnessAndResistance(1.5F, 12))
                    .tag(ZYTags.Blocks.ZYCHORIUM_PLATE)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.tag(Tags.Items.INGOTS_IRON), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIUM_SHIELD = zyBlock("{type}_zychorium_shield", (type, builder) ->
            builder.properties(properties -> properties.hardnessAndResistance(1.5F, 1200))
                    .tag(ZYTags.Blocks.ZYCHORIUM_SHIELD)
                    .recipe((ctx, provider) ->
                            infused(provider, DataIngredient.tag(Tags.Items.OBSIDIAN), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<ZychoriumLampBlock> ZYCHORIUM_LAMP = lamp(false);

    public static final BlockEntry<ZychoriumLampBlock> INVERTED_ZYCHORIUM_LAMP = lamp( true);

    public static final BlockEntry<ColorableBlock> IMMORTAL_BLOCK = REGISTRY.object("immortal_block")
            .block(ColorableBlock::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/zy_cube_all"))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.tag(ZYTags.Items.STORAGE_BLOCKS_ZYCHORIUM), ctx::getEntry))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .register();

    public static final BlockEntry<ColorableBlock> THE_AUREY_BLOCK = REGISTRY.object("the_aurey_block")
            .block(ColorableBlock::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/colorable_cube_all"))
                    .texture("base", provider.modLoc("block/" + ctx.getName()))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .recipe((ctx, provider) ->
                    colorable(provider, DataIngredient.items(ALUMINIUM_BLOCK), ctx::getEntry))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .register();

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> VIEWER = viewer(false);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> IMMORTAL_VIEWER = immortalViewer(false);

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> PHANTOMIZED_VIEWER = viewer(true);

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> PHANTOMIZED_IMMORTAL_VIEWER = immortalViewer(true);

    public static final Map<ZYType, BlockEntry<ZYBlock>> ZYCHORIZED_ENGINEERING_BLOCK = zyBlock("zychorized_{type}_engineering_block", (type, builder) ->
            builder.addLayer(() -> RenderType::getTranslucent)
                    .tag(ZYTags.Blocks.ZYCHORIZED_ENGINEERING_BLOCK)
                    .recipe((ctx, provider) ->
                            engineering(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<ZYBlock>> ALUMINIZED_ENGINEERING_BLOCK = zyBlock("aluminized_{type}_engineering_block", (type, builder) ->
            builder.addLayer(() -> RenderType::getTranslucent)
                    .tag(ZYTags.Blocks.ALUMINIZED_ENGINEERING_BLOCK)
                    .recipe((ctx, provider) ->
                            engineering(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_WATER = basicMachine("zychorium_water", ZYType.BLUE);
    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_SOIL = basicMachine("zychorium_soil", ZYType.GREEN);
    public static final BlockEntry<BasicMachineBlock> FIRE_BASIN = basicMachine("fire_basin", ZYType.RED);
    public static final BlockEntry<BasicMachineBlock> FLUID_VOID = basicMachine("fluid_void", ZYType.DARK);
    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_ICE = basicMachine("zychorium_ice", ZYType.LIGHT);

    public static final BlockEntry<Block> FABRICATOR = REGISTRY.object("fabricator")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::fabricatorBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/machine"))
                    .texture("side", provider.modLoc("block/fabricator_side"))
                    .texture("top", provider.modLoc("block/fabricator_top"))))
            .item()
                .color(() -> ZYColors::fabricatorItemColor)
                .build()
            .register();

    public static final ImmutableMap<MultiChildMaterial, BlockEntry<DefaultMultiChildBlock>> DEFAULT_MULTI_CHILD = defaultMultiChild();

    public static final BlockEntry<TestBlock> VALVE = REGISTRY.object("valve")
            .block(TestBlock::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::valveBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/multi_interface"))
                    .texture("overlay", provider.modLoc("block/valve"))))
            .simpleTileEntity(TestTile::new)
            .item()
                .color(() -> ZYColors::valveItemColor)
                .build()
            .register();

    public static final BlockEntry<Block> ITEM_IO = REGISTRY.object("item_io")
            .block(Block::new)
            .initialProperties(ZYCHORITE)
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::itemIOBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/multi_interface"))
                    .texture("overlay", provider.modLoc("block/item_io"))))
            .item()
                .color(() -> ZYColors::itemIOItemColor)
                .build()
            .lang("Item IO")
            .register();

    private static ImmutableMap<ZYType, BlockEntry<ZYBlock>> zyBlock(String pattern, NonNullBiFunction<ZYType, BlockBuilder<ZYBlock, Registrate>, BlockBuilder<ZYBlock, Registrate>> factory)
    {
        return zyBase(pattern, false, factory);
    }

    private static ImmutableMap<ZYType, BlockEntry<ZYBlock>> zyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<ZYBlock, Registrate>, BlockBuilder<ZYBlock, Registrate>> factory)
    {
        return zyBase(pattern, true, factory);
    }

    private static ImmutableMap<ZYType, BlockEntry<ZYBlock>> zyBase(String pattern, boolean bricks, NonNullBiFunction<ZYType, BlockBuilder<ZYBlock, Registrate>, BlockBuilder<ZYBlock, Registrate>> factory)
    {
        ImmutableMap.Builder<ZYType, BlockEntry<ZYBlock>> blocks = ImmutableMap.builder();
        String name = pattern.replace("{type}_", "");

        for (ZYType type : ZYType.VALUES)
        {
            blocks.put(type, factory.apply(type, REGISTRY.object(pattern.replace("{type}", type.getName()))
                    .block(properties -> new ZYBlock(type, properties))
                    .initialProperties(ZYCHORITE)
                    .addLayer(() -> RenderType::getCutout)
                    .color(() -> () -> ZYColors.zyBlockColor(type, bricks))
                    .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                            .withExistingParent(name, provider.modLoc("block/" + (bricks ? "zy_bricks" : "zy_cube_all")))
                            .texture("all", provider.modLoc("block/" + name)))))
                    .item()
                        .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                        .color(() -> () -> ZYColors.zyItemColor(type, bricks))
                        .build()
                    .register());
        }

        return blocks.build();
    }

    private static ImmutableMap<ZYType, BlockEntry<Block>> solidZyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> factory)
    {
        ImmutableMap.Builder<ZYType, BlockEntry<Block>> blocks = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
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
                .color(() -> ZYColors::colorableBlockColor)
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
                            .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                            .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                            .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                            .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                            .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                            .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));
                })
                .tag(ZYTags.Blocks.ZYCHORIUM_LAMPS)
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                    .color(() -> () -> ZYColors.zyLampItemColor(inverted))
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
                    .color(() -> ZYColors::colorableBlockColor)
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

    private static BlockEntry<BasicMachineBlock> basicMachine(String name, ZYType type)
    {
        return REGISTRY.object(name)
                .block(properties -> new BasicMachineBlock(type, properties))
                .initialProperties(ZYCHORITE)
                .addLayer(() -> RenderType::getCutout)
                .color(() -> () -> ZYColors.zyBlockColor(type, false))
                .blockstate((ctx, provider) ->
                {
                    ModelFile model = null;

                    switch (type)
                    {
                        case BLUE:
                        case LIGHT:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/machine"))
                                    .texture("side", provider.modLoc("block/" + name))
                                    .texture("top", provider.modLoc("block/machine_base"));
                            break;
                        case GREEN:
                        case RED:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/machine"))
                                    .texture("side", provider.modLoc("block/" + name + "_side"))
                                    .texture("top", provider.modLoc("block/" + name + "_top"));
                            break;
                        case DARK:
                            model = provider.models()
                                    .withExistingParent(name, provider.modLoc("block/zy_cube_all"))
                                    .texture("all", provider.modLoc("block/" + name));
                            break;
                    }

                    provider.simpleBlock(ctx.getEntry(), model);
                })
                .item()
                    .color(() -> () -> ZYColors.zyItemColor(type, false))
                    .build()
                .recipe((ctx, provider) ->
                {
                    DataIngredient core = null;

                    switch (type)
                    {
                        case BLUE:
                            core = DataIngredient.items(Items.WATER_BUCKET);
                            break;
                        case GREEN:
                            core = DataIngredient.tag(ItemTags.SAPLINGS);
                            break;
                        case RED:
                            core = DataIngredient.tag(Tags.Items.GUNPOWDER);
                            break;
                        case DARK:
                            core = DataIngredient.items(Items.BUCKET);
                            break;
                        case LIGHT:
                            core = DataIngredient.items(Items.SNOWBALL);
                            break;
                    }

                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry())
                            .patternLine("#T#")
                            .patternLine("SCS")
                            .patternLine("#S#")
                            .key('#', DataIngredient.items(ZYCHORIZED_ENGINEERING_BLOCK.get(type)))
                            .key('T', type == ZYType.RED || type == ZYType.DARK ? DataIngredient.items(Items.IRON_BARS) : DataIngredient.items(ZYCHORITE))
                            .key('S', type == ZYType.DARK ? DataIngredient.items(Items.IRON_BARS) : DataIngredient.items(ZYCHORITE))
                            .key('C', core)
                            .addCriterion("has_" + provider.safeName(core), core.getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));
                })
                .register();
    }

    private static ImmutableMap<MultiChildMaterial, BlockEntry<DefaultMultiChildBlock>> defaultMultiChild()
    {
        ImmutableMap.Builder<MultiChildMaterial, BlockEntry<DefaultMultiChildBlock>> blocks = ImmutableMap.builder();

        for (MultiChildMaterial material : MultiChildMaterial.VALUES)
        {
            blocks.put(material, REGISTRY.object(material.getName() + "_multi_child")
                    .block(material.material(), DefaultMultiChildBlock::new)
                    .properties(properties -> properties.noDrops().variableOpacity())
                    .properties(properties -> material.notSolid() ? properties.notSolid() : properties)
                    .color(() -> ZYColors::defaultMultiChildColor)
                    .blockstate((ctx, provider) ->
                            provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(provider.modLoc("block/default_multi_child"))))
                    .loot((ctx, provider) -> NonNullBiConsumer.noop())
                    //.tag(ZYTags.Blocks.FAKE_CHILD)
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
                .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    public static void init() {}
}
