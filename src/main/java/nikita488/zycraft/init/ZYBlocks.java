package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.*;
import nikita488.zycraft.client.ZYColors;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.enums.ZYType;

import java.util.Map;
import java.util.function.Supplier;

import static nikita488.zycraft.util.EnumUtils.viewerBase;
import static nikita488.zycraft.util.EnumUtils.zyBase;

public class ZYBlocks
{
    private static final Registrate REGISTRATE = ZYCraft.registrate().itemGroup(() -> ZYGroups.BLOCKS, "ZYCraft Blocks");

    public static final BlockEntry<Block> ZYCHORITE = REGISTRATE.block("zychorite", Block::new)
            .initialProperties(Material.ROCK, MaterialColor.BLACK)
            .properties(properties -> properties
                    .setRequiresTool()
                    .hardnessAndResistance(1.5F, 6))
            .tag(Tags.Blocks.STONE)
            .item()
                .tag(Tags.Items.STONE, ItemTags.STONE_TOOL_MATERIALS, ItemTags.STONE_CRAFTING_MATERIALS)
                .build()
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BLOCK = REGISTRATE.block("zychorite_block", Block::new)
            .initialProperties(ZYCHORITE)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .item()
                .tag(Tags.Items.STORAGE_BLOCKS)
                .build()
            .recipe((ctx, provider) -> provider.storage(ZYCHORITE, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ZYCHORITE_BRICKS = REGISTRATE.block("zychorite_bricks", Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, ZYCHORITE, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ZYCHORITE_BRICKS = REGISTRATE.block("small_zychorite_bricks", Block::new)
            .initialProperties(ZYCHORITE)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, ZYCHORITE_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_ORE = REGISTRATE.block("aluminium_ore", Block::new)
            .properties(properties -> properties
                    .setRequiresTool()
                    .hardnessAndResistance(3, 3))
            .tag(ZYTags.Blocks.ORES_ALUMINIUM, ZYTags.Blocks.ORES_ALUMINUM)
            .simpleItem()
            .recipe((ctx, provider) -> provider.smeltingAndBlasting(DataIngredient.items(ctx.getEntry()), ZYItems.ALUMINIUM, 0.1F))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BLOCK = REGISTRATE.block("aluminium_block", Block::new)
            .initialProperties(Material.ROCK, MaterialColor.QUARTZ)
            .properties(properties -> properties
                    .setRequiresTool()
                    .hardnessAndResistance(1.5F, 6))
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .item()
                .tag(Tags.Items.STORAGE_BLOCKS)
                .build()
            .recipe((ctx, provider) -> provider.storage(ZYItems.ALUMINIUM, ctx::getEntry))
            .register();

    public static final BlockEntry<Block> ALUMINIUM_BRICKS = REGISTRATE.block("aluminium_bricks", Block::new)
            .initialProperties(ALUMINIUM_BLOCK)
            .simpleItem()
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<Block> SMALL_ALUMINIUM_BRICKS = REGISTRATE.block("small_aluminium_bricks", Block::new)
            .initialProperties(ALUMINIUM_BLOCK)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, ALUMINIUM_BRICKS, ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalClusterBlock> QUARTZ_CRYSTAL = REGISTRATE.block("quartz_crystal", QuartzCrystalClusterBlock::new)
            .initialProperties(Material.MISCELLANEOUS, MaterialColor.DIAMOND)
            .properties(properties -> properties
                    .hardnessAndResistance(0.3F)
                    .setLightLevel(state -> 9)
                    .sound(SoundType.GLASS)
                    .notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .loot((tables, block) -> tables.registerLootTable(block, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .acceptCondition(SurvivesExplosion.builder())
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(block)
                                    .acceptFunction(SetCount.builder(ConstantRange.of(2))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 2))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(3))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 3))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(4))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 4))))
                                    .acceptFunction(SetCount.builder(ConstantRange.of(5))
                                            .acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder()
                                                    .withIntProp(QuartzCrystalClusterBlock.AMOUNT, 5))))))))
            .blockstate((ctx, provider) -> NonNullConsumer.noop())
            .item()
                .model((ctx, provider) -> provider.blockItem(ctx::getEntry))
                .build()
            .register();

    public static final BlockEntry<QuartzCrystalBlock> QUARTZ_CRYSTAL_BLOCK = REGISTRATE.block("quartz_crystal_block", QuartzCrystalBlock::new)
            .initialProperties(Material.GLASS, MaterialColor.DIAMOND)
            .properties(properties -> properties
                    .hardnessAndResistance(0.3F)
                    .setLightLevel(state -> 9)
                    .sound(SoundType.GLASS)
                    .notSolid())
            .addLayer(() -> RenderType::getTranslucent)
            .tag(Tags.Blocks.STORAGE_BLOCKS, BlockTags.IMPERMEABLE)
            .item()
                .tag(Tags.Items.STORAGE_BLOCKS)
                .build()
            .recipe((ctx, provider) -> provider.storage(QUARTZ_CRYSTAL, ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalBlock> QUARTZ_CRYSTAL_BRICKS = REGISTRATE.block("quartz_crystal_bricks", QuartzCrystalBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .tag(BlockTags.IMPERMEABLE)
            .simpleItem()
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(QUARTZ_CRYSTAL), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry))
            .register();

    public static final BlockEntry<QuartzCrystalBlock> SMALL_QUARTZ_CRYSTAL_BRICKS = REGISTRATE.block("small_quartz_crystal_bricks", QuartzCrystalBlock::new)
            .initialProperties(QUARTZ_CRYSTAL_BLOCK)
            .addLayer(() -> RenderType::getTranslucent)
            .tag(BlockTags.IMPERMEABLE)
            .simpleItem()
            .recipe((ctx, provider) -> bricks(provider, QUARTZ_CRYSTAL_BRICKS, ctx::getEntry))
            .register();

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_ORE = zyBlock("{type}_zychorium_ore", (type, block) -> block
            .properties(properties -> properties.setRequiresTool().hardnessAndResistance(3, 3))
            .tag(ZYTags.Blocks.ORES_ZYCHORIUM)
            .loot((tables, ore) -> tables.registerLootTable(ore, RegistrateBlockLootTables.droppingWithSilkTouch(ore,
                    ItemLootEntry.builder(ZYItems.ZYCHORIUM.get(type).get())
                            .acceptFunction(ExplosionDecay.builder())
                            .acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))
                            .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))))));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_BLOCK = zyBlock("{type}_zychorium_block", (type, block) -> block
            .tag(ZYTags.Blocks.STORAGE_BLOCKS_ZYCHORIUM)
            .recipe((ctx, provider) -> provider.storage(ZYItems.ZYCHORIUM.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_BRICKS = zyBricks("{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ZYCHORIUM)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(ZYItems.ZYCHORIUM.get(type)), DataIngredient.tag(ItemTags.STONE_BRICKS), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ZYCHORIUM_BRICKS = zyBricks("small_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIUM)
            .recipe((ctx, provider) -> bricks(provider, ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SOLID_ZYCHORIUM_BRICKS = solidZyBricks("solid_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_SOLID_ZYCHORIUM)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(Blocks.STONE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_SOLID_ZYCHORIUM_BRICKS = solidZyBricks("small_solid_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM)
            .recipe((ctx, provider) -> bricks(provider, SOLID_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("zychorized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ZYCHORIZED_ZYCHORIUM_BRICKS = zyBlock("small_zychorized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> bricks(provider, ZYCHORIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("aluminized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.BRICKS_ALUMINIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> SMALL_ALUMINIZED_ZYCHORIUM_BRICKS = zyBlock("small_aluminized_{type}_zychorium_bricks", (type, block) -> block
            .tag(ZYTags.Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM)
            .recipe((ctx, provider) -> bricks(provider, ALUMINIZED_ZYCHORIUM_BRICKS.get(type), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_PLATE = zyBlock("{type}_zychorium_plate", (type, block) -> block
            .properties(properties -> properties.hardnessAndResistance(1.5F, 12))
            .tag(ZYTags.Blocks.ZYCHORIUM_PLATE)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.tag(Tags.Items.INGOTS_IRON), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIUM_SHIELD = zyBlock("{type}_zychorium_shield", (type, block) -> block
            .properties(properties -> properties.hardnessAndResistance(1.5F, 1200))
            .tag(ZYTags.Blocks.ZYCHORIUM_SHIELD)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.tag(Tags.Items.OBSIDIAN), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<ZychoriumLampBlock> ZYCHORIUM_LAMP = REGISTRATE.block("zychorium_lamp", properties -> new ZychoriumLampBlock(false, properties))
            .transform(ZYBlocks::zychoriumLampProperties)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .singleTexture(ctx.getName(), provider.modLoc("block/zy_cube_all"), "all", provider.blockTexture(ctx.getEntry()))))
            .item()
                .color(() -> () -> ZYColors.zyLampItemColor(false))
                .build()
            .recipe((ctx, provider) -> lamp(provider, ctx::getEntry, false))
            .register();

    public static final BlockEntry<ZychoriumLampBlock> INVERTED_ZYCHORIUM_LAMP = REGISTRATE.block("inverted_zychorium_lamp", properties -> new ZychoriumLampBlock(true, properties))
            .transform(ZYBlocks::zychoriumLampProperties)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(ZYCHORIUM_LAMP.getId())))
            .item()
                .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + provider.name(ZYCHORIUM_LAMP))))
                .color(() -> () -> ZYColors.zyLampItemColor(true))
                .build()
            .recipe((ctx, provider) -> lamp(provider, ctx::getEntry, true))
            .register();

    public static final BlockEntry<ColorableBlock> IMMORTAL_BLOCK = REGISTRATE.block("immortal_block", ColorableBlock::new)
            .initialProperties(Material.ROCK, MaterialColor.SNOW)
            .properties(properties -> properties
                    .setRequiresTool()
                    .hardnessAndResistance(1.5F, 6)
                    .setAllowsSpawn((state, level, pos, entity) -> false))
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .singleTexture(ctx.getName(), provider.modLoc("block/zy_cube_all"), "all", provider.modLoc("block/zychorium_block"))))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .recipe((ctx, provider) -> colorable(provider, DataIngredient.tag(ZYTags.Items.STORAGE_BLOCKS_ZYCHORIUM), ctx::getEntry))
            .register();

    public static final BlockEntry<ColorableBlock> THE_AUREY_BLOCK = REGISTRATE.block("the_aurey_block", ColorableBlock::new)
            .initialProperties(Material.ROCK, MaterialColor.SNOW)
            .properties(properties -> properties
                    .setRequiresTool()
                    .hardnessAndResistance(1.5F, 6))
            .addLayer(() -> RenderType::getCutout)
            .color(() -> ZYColors::colorableBlockColor)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/colorable_cube_all"))
                    .texture("base", provider.blockTexture(ctx.getEntry()))
                    .texture("all", provider.modLoc("block/zychorium_block"))))
            .tag(ZYTags.Blocks.COLORABLE)
            .simpleItem()
            .recipe((ctx, provider) -> colorable(provider, DataIngredient.items(ALUMINIUM_BLOCK), ctx::getEntry))
            .register();

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> VIEWER = viewer("{type}_viewer", (type, block) -> block
            .tag(ZYTags.Blocks.VIEWERS_BASE)
            .recipe((ctx, provider) ->
            {
                if (type == ViewerType.BASIC)
                    ShapedRecipeBuilder.shapedRecipe(ctx.getEntry(), 8)
                            .key('#', Tags.Items.GLASS)
                            .key('X', type.ingredient())
                            .patternLine("###")
                            .patternLine("#X#")
                            .patternLine("###")
                            .addCriterion("has_" + provider.safeName(type.ingredient()), type.ingredient().getCritereon(provider))
                            .build(provider, provider.safeId(ctx.getEntry()));
                else
                    infused(provider, type.ingredient(), DataIngredient.items(basicViewer()), ctx::getEntry);
            })
    );

    public static final Map<ViewerType, BlockEntry<ViewerBlock>> PHANTOMIZED_VIEWER = viewer("phantomized_{type}_viewer", (type, block) -> block
            .properties(AbstractBlock.Properties::doesNotBlockMovement)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(VIEWER.get(type).getId())))
            .tag(ZYTags.Blocks.VIEWERS_PHANTOMIZED)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(VIEWER.get(type)), ctx::getEntry))
    );

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> IMMORTAL_VIEWER = immortalViewer("{type}_immortal_viewer", (type, block) -> block
            .blockstate((ctx, provider) ->
            {
                ResourceLocation name = basicImmortalViewer().getId();

                if (type == ViewerType.BASIC)
                    provider.simpleBlock(ctx.getEntry(), provider.models()
                            .withExistingParent(name.getPath(), provider.modLoc("block/colorable_cube_all"))
                            .texture("base", provider.modLoc("block/immortal_viewer_base"))
                            .texture("all", provider.modLoc("block/immortal_viewer_overlay")));
                else
                    provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(name));
            })
            .tag(ZYTags.Blocks.VIEWERS_IMMORTAL)
            .recipe((ctx, provider) ->
            {
                if (type == ViewerType.BASIC)
                    colorable(provider, DataIngredient.items(VIEWER.get(ViewerType.BASIC)), ctx::getEntry);
                else
                    infused(provider, type.ingredient(), DataIngredient.items(basicImmortalViewer()), ctx::getEntry);
            })
    );

    public static final Map<ViewerType, BlockEntry<ImmortalViewerBlock>> PHANTOMIZED_IMMORTAL_VIEWER = immortalViewer("phantomized_{type}_immortal_viewer", (type, block) -> block
            .properties(AbstractBlock.Properties::doesNotBlockMovement)
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(basicImmortalViewer().getId())))
            .tag(ZYTags.Blocks.VIEWERS_PHANTOMIZED_IMMORTAL)
            .recipe((ctx, provider) -> infused(provider, DataIngredient.items(Items.PHANTOM_MEMBRANE), DataIngredient.items(IMMORTAL_VIEWER.get(type)), ctx::getEntry))
    );

    public static final Map<ZYType, BlockEntry<Block>> ZYCHORIZED_ENGINEERING_BLOCK = zyBlock("zychorized_{type}_engineering_block", (type, block) -> block
            .addLayer(() -> RenderType::getTranslucent)
            .tag(ZYTags.Blocks.ZYCHORIZED_ENGINEERING_BLOCK)
            .recipe((ctx, provider) -> engineering(provider, DataIngredient.items(ZYCHORITE), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final Map<ZYType, BlockEntry<Block>> ALUMINIZED_ENGINEERING_BLOCK = zyBlock("aluminized_{type}_engineering_block", (type, block) -> block
            .addLayer(() -> RenderType::getTranslucent)
            .tag(ZYTags.Blocks.ALUMINIZED_ENGINEERING_BLOCK)
            .recipe((ctx, provider) -> engineering(provider, DataIngredient.items(ZYItems.ALUMINIUM), DataIngredient.items(ZYCHORIUM_BRICKS.get(type)), ctx::getEntry)));

    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_WATER = REGISTRATE.block("zychorium_water", properties -> new BasicMachineBlock(ZYType.BLUE, properties))
            .transform(builder -> basicMachineProperties(builder, ZYType.BLUE))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .singleTexture(ctx.getName(), provider.modLoc("block/basic_machine_side"), "side", provider.blockTexture(ctx.getEntry()))))
            .recipe((ctx, provider) -> basicMachine(ZYType.BLUE, provider, DataIngredient.items(Items.WATER_BUCKET), ctx::getEntry))
            .register();

    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_SOIL = REGISTRATE.block("zychorium_soil", properties -> new BasicMachineBlock(ZYType.GREEN, properties))
            .transform(builder -> basicMachineProperties(builder, ZYType.GREEN))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/basic_machine_top"))
                    .texture("side", provider.modLoc("block/" + ctx.getName() + "_side"))
                    .texture("top", provider.modLoc("block/" + ctx.getName() + "_top"))))
            .recipe((ctx, provider) -> basicMachine(ZYType.GREEN, provider, DataIngredient.tag(ItemTags.SAPLINGS), ctx::getEntry))
            .register();

    public static final BlockEntry<BasicMachineBlock> FIRE_BASIN = REGISTRATE.block("fire_basin", properties -> new BasicMachineBlock(ZYType.RED, properties))
            .transform(builder -> basicMachineProperties(builder, ZYType.RED))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .withExistingParent(ctx.getName(), provider.modLoc("block/basic_machine_top"))
                    .texture("side", provider.modLoc("block/" + ctx.getName() + "_side"))
                    .texture("top", provider.modLoc("block/" + ctx.getName() + "_top"))))
            .recipe((ctx, provider) -> basicMachine(ZYType.RED, provider, DataIngredient.tag(Tags.Items.GUNPOWDER), ctx::getEntry))
            .register();

    public static final BlockEntry<BasicMachineBlock> FLUID_VOID = REGISTRATE.block("fluid_void", properties -> new BasicMachineBlock(ZYType.DARK, properties))
            .transform(builder -> basicMachineProperties(builder, ZYType.DARK))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .singleTexture(ctx.getName(), provider.modLoc("block/zy_cube_all"), "all", provider.blockTexture(ctx.getEntry()))))
            .recipe((ctx, provider) -> basicMachine(ZYType.DARK, provider, DataIngredient.items(Items.BUCKET), ctx::getEntry))
            .register();

    public static final BlockEntry<BasicMachineBlock> ZYCHORIUM_ICE = REGISTRATE.block("zychorium_ice", properties -> new BasicMachineBlock(ZYType.LIGHT, properties))
            .transform(builder -> basicMachineProperties(builder, ZYType.LIGHT))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                    .singleTexture(ctx.getName(), provider.modLoc("block/basic_machine_side"), "side", provider.blockTexture(ctx.getEntry()))))
            .recipe((ctx, provider) -> basicMachine(ZYType.LIGHT, provider, DataIngredient.items(Items.SNOWBALL), ctx::getEntry))
            .register();

    private static Map<ZYType, BlockEntry<Block>> zyBlock(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> transform)
    {
        return zyBlock(pattern, false, transform);
    }

    private static Map<ZYType, BlockEntry<Block>> zyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> transform)
    {
        return zyBlock(pattern, true, transform);
    }

    private static Map<ZYType, BlockEntry<Block>> zyBlock(String pattern, boolean coloredOverlay, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> transform)
    {
        String name = pattern.replace("{type}_", "");
        return zyBase(pattern, (type, registryName) -> REGISTRATE.block(registryName, Block::new)
                .initialProperties(Material.ROCK, type.mtlColor())
                .properties(properties -> properties
                        .setRequiresTool()
                        .hardnessAndResistance(1.5F, 6)
                        .setAllowsSpawn((state, level, pos, entity) -> false))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> () -> ZYColors.zyBlockColor(type, coloredOverlay))
                .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models()
                        .singleTexture(name, provider.modLoc("block/" + (coloredOverlay ? "zy_bricks" : "zy_cube_all")), "all", provider.modLoc("block/" + name))))
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + name)))
                    .color(() -> () -> ZYColors.zyItemColor(type, coloredOverlay))
                    .build()
                .transform(builder -> transform.apply(type, builder))
                .register());
    }

    private static Map<ZYType, BlockEntry<Block>> solidZyBricks(String pattern, NonNullBiFunction<ZYType, BlockBuilder<Block, Registrate>, BlockBuilder<Block, Registrate>> transform)
    {
        return zyBase(pattern, (type, registryName) -> REGISTRATE.block(registryName, Block::new)
                .initialProperties(Material.ROCK, type.mtlColor())
                .properties(properties -> properties
                        .setRequiresTool()
                        .hardnessAndResistance(1.5F, 6))
                .simpleItem()
                .transform(builder -> transform.apply(type, builder))
                .register());
    }

    private static BlockBuilder<ZychoriumLampBlock, Registrate> zychoriumLampProperties(BlockBuilder<ZychoriumLampBlock, Registrate> builder)
    {
        return builder.initialProperties(Material.ROCK, MaterialColor.SNOW)
                .properties(properties -> properties.hardnessAndResistance(0.3F, 6)
                        .setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 15 : 0)
                        .sound(SoundType.GLASS)
                        .setAllowsSpawn((state, level, pos, entity) -> false))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> ZYColors::colorableBlockColor)
                .tag(ZYTags.Blocks.ZYCHORIUM_LAMPS);
    }

    private static Map<ViewerType, BlockEntry<ViewerBlock>> viewer(String pattern, NonNullBiFunction<ViewerType, BlockBuilder<ViewerBlock, Registrate>, BlockBuilder<ViewerBlock, Registrate>> transform)
    {
        return viewerBlock(pattern, false, ViewerBlock::new, (type, block) -> transform.apply(type, block
                .addLayer(() -> () -> type == ViewerType.DARK ? RenderType.getTranslucent() : RenderType.getCutout())
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + provider.name(VIEWER.get(type)))))
                    .build()));
    }

    private static Map<ViewerType, BlockEntry<ImmortalViewerBlock>> immortalViewer(String pattern, NonNullBiFunction<ViewerType, BlockBuilder<ImmortalViewerBlock, Registrate>, BlockBuilder<ImmortalViewerBlock, Registrate>> transform)
    {
        return viewerBlock(pattern, true, ImmortalViewerBlock::new, (type, block) -> transform.apply(type, block
                .addLayer(() -> RenderType::getTranslucent)
                .color(() -> ZYColors::colorableBlockColor)
                .item()
                    .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/" + provider.name(basicImmortalViewer()))))
                    .build()));
    }

    private static <T extends Block> Map<ViewerType, BlockEntry<T>> viewerBlock(String pattern, boolean immortal, NonNullBiFunction<ViewerType, Block.Properties, T> blockFactory, NonNullBiFunction<ViewerType, BlockBuilder<T, Registrate>, BlockBuilder<T, Registrate>> transform)
    {
        return viewerBase(pattern, immortal, (type, registryName) -> REGISTRATE.block(registryName, properties -> blockFactory.apply(type, properties))
                .initialProperties(() -> Blocks.GLASS)
                .properties(type.properties())
                .transform(builder -> transform.apply(type, builder))
                .register());
    }

    private static BlockBuilder<BasicMachineBlock, Registrate> basicMachineProperties(BlockBuilder<BasicMachineBlock, Registrate> builder, ZYType type)
    {
        return builder.initialProperties(Material.ROCK, type.mtlColor())
                .properties(properties -> properties
                        .setRequiresTool()
                        .hardnessAndResistance(1.5F, 6)
                        .setAllowsSpawn((state, level, pos, entity) -> false))
                .addLayer(() -> RenderType::getCutout)
                .color(() -> () -> ZYColors.zyBlockColor(type, false))
                .tag(ZYTags.Blocks.BASIC_MACHINES)
                .item()
                    .color(() -> () -> ZYColors.zyItemColor(type, false))
                    .build();
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void square(RegistrateRecipeProvider provider, DataIngredient source, Supplier<? extends T> result, int count, boolean small)
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(result.get(), count).key('X', source);

        if (small)
            builder.patternLine("XX")
                    .patternLine("XX");
        else
            builder.patternLine("XXX")
                    .patternLine("XXX")
                    .patternLine("XXX");

        builder.addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void bricks(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> source, NonNullSupplier<? extends T> result)
    {
        bricks(provider, DataIngredient.items(source), result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void bricks(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        square(provider, source, result, 4, true);
        provider.stonecutting(source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void infused(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('I', infusionSource)
                .key('#', source)
                .patternLine("I#I")
                .patternLine("# #")
                .patternLine("I#I")
                .addCriterion("has_" + provider.safeName(infusionSource), infusionSource.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void engineering(RegistrateRecipeProvider provider, DataIngredient infusionSource, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        DataIngredient core = DataIngredient.tag(Tags.Items.DUSTS_REDSTONE);

        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('I', infusionSource)
                .key('#', source)
                .key('X', core)
                .patternLine("I#I")
                .patternLine("#X#")
                .patternLine("I#I")
                .addCriterion("has_" + provider.safeName(core), core.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void colorable(RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        colorable(provider.safeId(result.get()), provider, source, result);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void colorable(ResourceLocation name, RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
                .key('#', source)
                .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                .patternLine("#L#")
                .patternLine("RGB")
                .patternLine("#D#")
                .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider))
                .build(provider, name);
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void lamp(RegistrateRecipeProvider provider, NonNullSupplier<? extends T> result, boolean inverted)
    {
        DataIngredient quartzCrystalSource = DataIngredient.items(QUARTZ_CRYSTAL);

        ShapedRecipeBuilder.shapedRecipe(result.get())
                .key('X', Tags.Items.GLASS)
                .key('#', quartzCrystalSource)
                .key('$', inverted ? Ingredient.fromItems(Items.REDSTONE_TORCH) : Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE))
                .key('R', ZYItems.ZYCHORIUM.get(ZYType.RED).get())
                .key('G', ZYItems.ZYCHORIUM.get(ZYType.GREEN).get())
                .key('B', ZYItems.ZYCHORIUM.get(ZYType.BLUE).get())
                .key('D', ZYItems.ZYCHORIUM.get(ZYType.DARK).get())
                .key('L', ZYItems.ZYCHORIUM.get(ZYType.LIGHT).get())
                .patternLine("RGB")
                .patternLine("#X#")
                .patternLine("D$L")
                .addCriterion("has_" + provider.safeName(quartzCrystalSource), quartzCrystalSource.getCritereon(provider))
                .build(provider, provider.safeId(result.get()));

        BlockEntry<ZychoriumLampBlock> from = inverted ? INVERTED_ZYCHORIUM_LAMP : ZYCHORIUM_LAMP;
        BlockEntry<ZychoriumLampBlock> to = inverted ? ZYCHORIUM_LAMP : INVERTED_ZYCHORIUM_LAMP;

        provider.singleItemUnfinished(DataIngredient.items(from), to, 1, 1)
                .build(provider, provider.safeId(to.getId()) + "_from_" + provider.safeName(from.getId()));
    }

    private static <T extends IItemProvider & IForgeRegistryEntry<?>> void basicMachine(ZYType type, RegistrateRecipeProvider provider, DataIngredient source, NonNullSupplier<? extends T> result)
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(result.get())
                .key('X', DataIngredient.tag(ZYTags.Items.ENGINEERING_BLOCKS.get(type)))
                .key('#', type == ZYType.DARK ? DataIngredient.items(Items.IRON_BARS) : DataIngredient.items(ZYCHORITE))
                .key('$', source)
                .patternLine(type == ZYType.RED ? "X%X" : "X#X")
                .patternLine("#$#")
                .patternLine("X#X")
                .addCriterion("has_" + provider.safeName(source), source.getCritereon(provider));

        if (type == ZYType.RED)
            builder.key('%', DataIngredient.items(Items.IRON_BARS));

        builder.build(provider, provider.safeId(result.get()));
    }

    private static BlockEntry<ViewerBlock> basicViewer()
    {
        return VIEWER.get(ViewerType.BASIC);
    }

    private static BlockEntry<ImmortalViewerBlock> basicImmortalViewer()
    {
        return IMMORTAL_VIEWER.get(ViewerType.BASIC);
    }

    public static void init() {}
}
