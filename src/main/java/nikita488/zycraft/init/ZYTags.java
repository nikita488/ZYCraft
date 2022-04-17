package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ZYType;

import java.util.Map;

public class ZYTags
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static class Blocks
    {
        public static final TagKey<Block> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final TagKey<Block> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final TagKey<Block> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final TagKey<Block> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final TagKey<Block> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final TagKey<Block> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final TagKey<Block> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final TagKey<Block> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final TagKey<Block> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final TagKey<Block> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final TagKey<Block> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final TagKey<Block> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final TagKey<Block> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final TagKey<Block> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final TagKey<Block> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final TagKey<Block> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final TagKey<Block> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final TagKey<Block> COLORABLE = tag("colorable");
        public static final TagKey<Block> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final TagKey<Block> VIEWERS = tag("viewers");

        public static final TagKey<Block> BASE_VIEWERS = tag("base_viewers");
        public static final TagKey<Block> VIEWERS_BASE = tag("viewers/base");
        public static final TagKey<Block> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final TagKey<Block> PAINTABLE_VIEWERS = tag("paintable_viewers");
        public static final TagKey<Block> VIEWERS_PAINTABLE = tag("viewers/paintable");
        public static final TagKey<Block> VIEWERS_PHANTOMIZED_PAINTABLE = tag("viewers/phantomized_paintable");

        public static final TagKey<Block> ENGINEERING_BRICKS = tag("engineering_bricks");
        public static final TagKey<Block> BRICKS_ZYCHORIZED_ENGINEERING = tag("bricks/zychorized_engineering");
        public static final TagKey<Block> BRICKS_ALUMINIZED_ENGINEERING = tag("bricks/aluminized_engineering");
        public static final TagKey<Block> SMALL_BRICKS_ZYCHORIZED_ENGINEERING = tag("small_bricks/zychorized_engineering");
        public static final TagKey<Block> SMALL_BRICKS_ALUMINIZED_ENGINEERING = tag("small_bricks/aluminized_engineering");

        public static final TagKey<Block> BASIC_MACHINES = tag("basic_machines");

        private static TagKey<Block> tag(String name)
        {
            return BlockTags.create(ZYCraft.id(name));
        }

        private static TagKey<Block> forgeTag(String name)
        {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

        private static Map<ZYType, TagKey<Block>> zyTag(String pattern)
        {
            return ZYType.buildMap(pattern, (type, name) -> tag(name));
        }
    }

    public static class Items
    {
        public static final TagKey<Item> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final TagKey<Item> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final TagKey<Item> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final TagKey<Item> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final TagKey<Item> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final TagKey<Item> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final TagKey<Item> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final TagKey<Item> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final TagKey<Item> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final TagKey<Item> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final TagKey<Item> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final TagKey<Item> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final TagKey<Item> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final TagKey<Item> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final TagKey<Item> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final TagKey<Item> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final TagKey<Item> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final TagKey<Item> COLORABLE = tag("colorable");
        public static final TagKey<Item> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final TagKey<Item> VIEWERS = tag("viewers");

        public static final TagKey<Item> BASE_VIEWERS = tag("base_viewers");
        public static final TagKey<Item> VIEWERS_BASE = tag("viewers/base");
        public static final TagKey<Item> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final TagKey<Item> PAINTABLE_VIEWERS = tag("paintable_viewers");
        public static final TagKey<Item> VIEWERS_PAINTABLE = tag("viewers/paintable");
        public static final TagKey<Item> VIEWERS_PHANTOMIZED_PAINTABLE = tag("viewers/phantomized_paintable");

        public static final TagKey<Item> ENGINEERING_BRICKS = tag("engineering_bricks");
        public static final TagKey<Item> BRICKS_ZYCHORIZED_ENGINEERING = tag("bricks/zychorized_engineering");
        public static final TagKey<Item> BRICKS_ALUMINIZED_ENGINEERING = tag("bricks/aluminized_engineering");
        public static final TagKey<Item> SMALL_BRICKS_ZYCHORIZED_ENGINEERING = tag("small_bricks/zychorized_engineering");
        public static final TagKey<Item> SMALL_BRICKS_ALUMINIZED_ENGINEERING = tag("small_bricks/aluminized_engineering");
        public static final Map<ZYType, TagKey<Item>> ENGINEERING_BRICKS_BY_TYPE = zyTag("engineering_bricks/{type}");

        public static final TagKey<Item> BASIC_MACHINES = tag("basic_machines");

        public static final TagKey<Item> ZYCHORIUM = tag("zychorium");

        private static TagKey<Item> tag(String name)
        {
            return ItemTags.create(ZYCraft.id(name));
        }

        private static TagKey<Item> forgeTag(String name)
        {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

        private static Map<ZYType, TagKey<Item>> zyTag(String pattern)
        {
            return ZYType.buildMap(pattern, (type, name) -> tag(name));
        }
    }

    public static void init()
    {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, tags ->
        {
            tags.tag(Blocks.BASE_ZYCHORIUM_BRICKS).addTags(Blocks.BRICKS_ZYCHORIUM,
                    Blocks.BRICKS_SOLID_ZYCHORIUM,
                    Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM,
                    Blocks.BRICKS_ALUMINIZED_ZYCHORIUM);

            tags.tag(Blocks.SMALL_ZYCHORIUM_BRICKS).addTags(Blocks.SMALL_BRICKS_ZYCHORIUM,
                    Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM,
                    Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM,
                    Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

            tags.tag(Blocks.ZYCHORIUM_BRICKS).addTags(Blocks.BASE_ZYCHORIUM_BRICKS, Blocks.SMALL_ZYCHORIUM_BRICKS);
            tags.tag(Blocks.BASE_VIEWERS).addTags(Blocks.VIEWERS_BASE, Blocks.VIEWERS_PHANTOMIZED);
            tags.tag(Blocks.PAINTABLE_VIEWERS).addTags(Blocks.VIEWERS_PAINTABLE, Blocks.VIEWERS_PHANTOMIZED_PAINTABLE);
            tags.tag(Blocks.VIEWERS).addTags(Blocks.BASE_VIEWERS, Blocks.PAINTABLE_VIEWERS);
            tags.tag(Blocks.COLORABLE).addTags(Blocks.ZYCHORIUM_LAMPS, Blocks.PAINTABLE_VIEWERS);

            tags.tag(Blocks.ENGINEERING_BRICKS).addTags(Blocks.BRICKS_ZYCHORIZED_ENGINEERING,
                    Blocks.BRICKS_ALUMINIZED_ENGINEERING,
                    Blocks.SMALL_BRICKS_ZYCHORIZED_ENGINEERING,
                    Blocks.SMALL_BRICKS_ALUMINIZED_ENGINEERING);

            tags.tag(Tags.Blocks.ORES).addTags(Blocks.ORES_ZYCHORIUM, Blocks.ORES_ALUMINIUM, Blocks.ORES_ALUMINUM);
            tags.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(Blocks.STORAGE_BLOCKS_ZYCHORIUM);
            tags.tag(Tags.Blocks.GLASS_COLORLESS).addTag(Blocks.BASE_VIEWERS);
            tags.tag(Tags.Blocks.STAINED_GLASS).addTag(Blocks.PAINTABLE_VIEWERS);

            tags.tag(BlockTags.IMPERMEABLE).addTags(Blocks.VIEWERS);
            tags.tag(BlockTags.WITHER_IMMUNE).addTag(Blocks.ZYCHORIUM_SHIELD);
            tags.tag(BlockTags.DRAGON_IMMUNE).addTag(Blocks.ZYCHORIUM_SHIELD);
        });

        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, tags ->
        {
            tags.copy(Blocks.ORES_ZYCHORIUM, Items.ORES_ZYCHORIUM);
            tags.copy(Blocks.ORES_ALUMINIUM, Items.ORES_ALUMINIUM);
            tags.copy(Blocks.ORES_ALUMINUM, Items.ORES_ALUMINUM);

            tags.copy(Blocks.STORAGE_BLOCKS_ZYCHORIUM, Items.STORAGE_BLOCKS_ZYCHORIUM);

            tags.copy(Blocks.ZYCHORIUM_BRICKS, Items.ZYCHORIUM_BRICKS);

            tags.copy(Blocks.BASE_ZYCHORIUM_BRICKS, Items.BASE_ZYCHORIUM_BRICKS);
            tags.copy(Blocks.BRICKS_ZYCHORIUM, Items.BRICKS_ZYCHORIUM);
            tags.copy(Blocks.BRICKS_SOLID_ZYCHORIUM, Items.BRICKS_SOLID_ZYCHORIUM);
            tags.copy(Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM, Items.BRICKS_ZYCHORIZED_ZYCHORIUM);
            tags.copy(Blocks.BRICKS_ALUMINIZED_ZYCHORIUM, Items.BRICKS_ALUMINIZED_ZYCHORIUM);

            tags.copy(Blocks.SMALL_ZYCHORIUM_BRICKS, Items.SMALL_ZYCHORIUM_BRICKS);
            tags.copy(Blocks.SMALL_BRICKS_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIUM);
            tags.copy(Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM, Items.SMALL_BRICKS_SOLID_ZYCHORIUM);
            tags.copy(Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM);
            tags.copy(Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM, Items.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

            tags.copy(Blocks.ZYCHORIUM_PLATE, Items.ZYCHORIUM_PLATE);
            tags.copy(Blocks.ZYCHORIUM_SHIELD, Items.ZYCHORIUM_SHIELD);

            tags.copy(Blocks.COLORABLE, Items.COLORABLE);
            tags.copy(Blocks.ZYCHORIUM_LAMPS, Items.ZYCHORIUM_LAMPS);

            tags.copy(Blocks.VIEWERS, Items.VIEWERS);

            tags.copy(Blocks.BASE_VIEWERS, Items.BASE_VIEWERS);
            tags.copy(Blocks.VIEWERS_BASE, Items.VIEWERS_BASE);
            tags.copy(Blocks.VIEWERS_PHANTOMIZED, Items.VIEWERS_PHANTOMIZED);

            tags.copy(Blocks.PAINTABLE_VIEWERS, Items.PAINTABLE_VIEWERS);
            tags.copy(Blocks.VIEWERS_PAINTABLE, Items.VIEWERS_PAINTABLE);
            tags.copy(Blocks.VIEWERS_PHANTOMIZED_PAINTABLE, Items.VIEWERS_PHANTOMIZED_PAINTABLE);

            tags.copy(Blocks.ENGINEERING_BRICKS, Items.ENGINEERING_BRICKS);
            tags.copy(Blocks.BRICKS_ZYCHORIZED_ENGINEERING, Items.BRICKS_ZYCHORIZED_ENGINEERING);
            tags.copy(Blocks.BRICKS_ALUMINIZED_ENGINEERING, Items.BRICKS_ALUMINIZED_ENGINEERING);
            tags.copy(Blocks.SMALL_BRICKS_ZYCHORIZED_ENGINEERING, Items.SMALL_BRICKS_ZYCHORIZED_ENGINEERING);
            tags.copy(Blocks.SMALL_BRICKS_ALUMINIZED_ENGINEERING, Items.SMALL_BRICKS_ALUMINIZED_ENGINEERING);

            for (ZYType type : ZYType.VALUES)
                tags.tag(Items.ENGINEERING_BRICKS_BY_TYPE.get(type)).add(ZYBlocks.ZYCHORIZED_ENGINEERING_BRICKS.get(type).get().asItem(),
                        ZYBlocks.SMALL_ZYCHORIZED_ENGINEERING_BRICKS.get(type).get().asItem(),
                        ZYBlocks.ALUMINIZED_ENGINEERING_BRICKS.get(type).get().asItem(),
                        ZYBlocks.SMALL_ALUMINIZED_ENGINEERING_BRICKS.get(type).get().asItem());

            tags.copy(Blocks.BASIC_MACHINES, Items.BASIC_MACHINES);

            tags.tag(Tags.Items.ORES).addTags(Items.ORES_ZYCHORIUM, Items.ORES_ALUMINIUM, Items.ORES_ALUMINUM);
            tags.tag(Tags.Items.STORAGE_BLOCKS).addTag(Items.STORAGE_BLOCKS_ZYCHORIUM);
            tags.tag(Tags.Items.GLASS_COLORLESS).addTag(Items.BASE_VIEWERS);
            tags.tag(Tags.Items.STAINED_GLASS).addTag(Items.PAINTABLE_VIEWERS);
            tags.tag(Tags.Items.GEMS).addTag(Items.ZYCHORIUM);
        });
    }
}
