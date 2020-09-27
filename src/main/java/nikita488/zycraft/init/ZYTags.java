package nikita488.zycraft.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import nikita488.zycraft.ZYCraft;

public class ZYTags
{
    public static class Blocks
    {
        public static final IOptionalNamedTag<Block> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final IOptionalNamedTag<Block> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final IOptionalNamedTag<Block> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final IOptionalNamedTag<Block> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final IOptionalNamedTag<Block> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final IOptionalNamedTag<Block> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final IOptionalNamedTag<Block> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final IOptionalNamedTag<Block> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final IOptionalNamedTag<Block> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final IOptionalNamedTag<Block> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final IOptionalNamedTag<Block> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final IOptionalNamedTag<Block> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final IOptionalNamedTag<Block> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final IOptionalNamedTag<Block> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final IOptionalNamedTag<Block> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final IOptionalNamedTag<Block> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final IOptionalNamedTag<Block> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final IOptionalNamedTag<Block> ENGINEERING_BLOCK = tag("engineering_block");
        public static final IOptionalNamedTag<Block> ZYCHORIZED_ENGINEERING_BLOCK = tag("zychorized_engineering_block");
        public static final IOptionalNamedTag<Block> ALUMINIZED_ENGINEERING_BLOCK = tag("aluminized_engineering_block");

        public static final IOptionalNamedTag<Block> COLORABLE = tag("colorable");
        public static final IOptionalNamedTag<Block> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final IOptionalNamedTag<Block> VIEWERS = tag("viewers");

        public static final IOptionalNamedTag<Block> BASE_VIEWERS = tag("base_viewers");
        public static final IOptionalNamedTag<Block> VIEWERS_BASE = tag("viewers/base");
        public static final IOptionalNamedTag<Block> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final IOptionalNamedTag<Block> IMMORTAL_VIEWERS = tag("immortal_viewers");
        public static final IOptionalNamedTag<Block> VIEWERS_IMMORTAL = tag("viewers/immortal");
        public static final IOptionalNamedTag<Block> VIEWERS_PHANTOMIZED_IMMORTAL = tag("viewers/phantomized_immortal");

        public static final IOptionalNamedTag<Block> BASIC_MACHINES = tag("basic_machines");

        private static IOptionalNamedTag<Block> tag(String name)
        {
            return BlockTags.createOptional(ZYCraft.modLoc(name));
        }

        private static IOptionalNamedTag<Block> forgeTag(String name)
        {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static class Items
    {
        public static final IOptionalNamedTag<Item> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final IOptionalNamedTag<Item> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final IOptionalNamedTag<Item> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final IOptionalNamedTag<Item> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final IOptionalNamedTag<Item> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final IOptionalNamedTag<Item> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final IOptionalNamedTag<Item> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final IOptionalNamedTag<Item> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final IOptionalNamedTag<Item> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final IOptionalNamedTag<Item> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final IOptionalNamedTag<Item> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final IOptionalNamedTag<Item> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final IOptionalNamedTag<Item> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final IOptionalNamedTag<Item> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final IOptionalNamedTag<Item> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final IOptionalNamedTag<Item> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final IOptionalNamedTag<Item> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final IOptionalNamedTag<Item> ENGINEERING_BLOCK = tag("engineering_block");
        public static final IOptionalNamedTag<Item> ZYCHORIZED_ENGINEERING_BLOCK = tag("zychorized_engineering_block");
        public static final IOptionalNamedTag<Item> ALUMINIZED_ENGINEERING_BLOCK = tag("aluminized_engineering_block");

        public static final IOptionalNamedTag<Item> COLORABLE = tag("colorable");
        public static final IOptionalNamedTag<Item> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final IOptionalNamedTag<Item> VIEWERS = tag("viewers");

        public static final IOptionalNamedTag<Item> BASE_VIEWERS = tag("base_viewers");
        public static final IOptionalNamedTag<Item> VIEWERS_BASE = tag("viewers/base");
        public static final IOptionalNamedTag<Item> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final IOptionalNamedTag<Item> IMMORTAL_VIEWERS = tag("immortal_viewers");
        public static final IOptionalNamedTag<Item> VIEWERS_IMMORTAL = tag("viewers/immortal");
        public static final IOptionalNamedTag<Item> VIEWERS_PHANTOMIZED_IMMORTAL = tag("viewers/phantomized_immortal");

        public static final IOptionalNamedTag<Item> BASIC_MACHINES = tag("basic_machines");

        public static final IOptionalNamedTag<Item> ZYCHORIUM = tag("zychorium");

        private static IOptionalNamedTag<Item> tag(String name)
        {
            return ItemTags.createOptional(ZYCraft.modLoc(name));
        }

        private static IOptionalNamedTag<Item> forgeTag(String name)
        {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }

    public static void init()
    {
        ZYCraft.REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, blockProvider ->
        {
            blockProvider.getOrCreateBuilder(Tags.Blocks.ORES)
                    .addTags(Blocks.ORES_ZYCHORIUM, Blocks.ORES_ALUMINIUM, Blocks.ORES_ALUMINUM);

            blockProvider.getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS)
                    .addTag(Blocks.STORAGE_BLOCKS_ZYCHORIUM);

            blockProvider.getOrCreateBuilder(Blocks.BASE_ZYCHORIUM_BRICKS)
                    .addTags(Blocks.BRICKS_ZYCHORIUM,
                            Blocks.BRICKS_SOLID_ZYCHORIUM,
                            Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM,
                            Blocks.BRICKS_ALUMINIZED_ZYCHORIUM);

            blockProvider.getOrCreateBuilder(Blocks.SMALL_ZYCHORIUM_BRICKS)
                    .addTags(Blocks.SMALL_BRICKS_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

            blockProvider.getOrCreateBuilder(Blocks.ZYCHORIUM_BRICKS)
                    .addTags(Blocks.BASE_ZYCHORIUM_BRICKS, Blocks.SMALL_ZYCHORIUM_BRICKS);

            blockProvider.getOrCreateBuilder(BlockTags.STONE_BRICKS)
                    .addTag(Blocks.ZYCHORIUM_BRICKS);

            blockProvider.getOrCreateBuilder(Blocks.ENGINEERING_BLOCK)
                    .addTags(Blocks.ZYCHORIZED_ENGINEERING_BLOCK, Blocks.ALUMINIZED_ENGINEERING_BLOCK);

            blockProvider.getOrCreateBuilder(Blocks.BASE_VIEWERS)
                    .addTags(Blocks.VIEWERS_BASE, Blocks.VIEWERS_PHANTOMIZED);

            blockProvider.getOrCreateBuilder(Blocks.IMMORTAL_VIEWERS)
                    .addTags(Blocks.VIEWERS_IMMORTAL, Blocks.VIEWERS_PHANTOMIZED_IMMORTAL);

            blockProvider.getOrCreateBuilder(Blocks.VIEWERS)
                    .addTags(Blocks.BASE_VIEWERS, Blocks.IMMORTAL_VIEWERS);

            blockProvider.getOrCreateBuilder(Tags.Blocks.GLASS_COLORLESS)
                    .addTag(Blocks.BASE_VIEWERS);

            blockProvider.getOrCreateBuilder(Tags.Blocks.STAINED_GLASS)
                    .addTag(Blocks.IMMORTAL_VIEWERS);

            blockProvider.getOrCreateBuilder(Blocks.COLORABLE)
                    .addTags(Blocks.ZYCHORIUM_LAMPS, Blocks.IMMORTAL_VIEWERS);

            ZYCraft.REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, itemProvider ->
            {
                copy(blockProvider, itemProvider, Blocks.ORES_ZYCHORIUM, Items.ORES_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.ORES_ALUMINIUM, Items.ORES_ALUMINIUM);
                copy(blockProvider, itemProvider, Blocks.ORES_ALUMINUM, Items.ORES_ALUMINUM);

                copy(blockProvider, itemProvider, Blocks.STORAGE_BLOCKS_ZYCHORIUM, Items.STORAGE_BLOCKS_ZYCHORIUM);

                copy(blockProvider, itemProvider, Blocks.ZYCHORIUM_BRICKS, Items.ZYCHORIUM_BRICKS);

                copy(blockProvider, itemProvider, Blocks.BASE_ZYCHORIUM_BRICKS, Items.BASE_ZYCHORIUM_BRICKS);
                copy(blockProvider, itemProvider, Blocks.BRICKS_ZYCHORIUM, Items.BRICKS_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.BRICKS_SOLID_ZYCHORIUM, Items.BRICKS_SOLID_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM, Items.BRICKS_ZYCHORIZED_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.BRICKS_ALUMINIZED_ZYCHORIUM, Items.BRICKS_ALUMINIZED_ZYCHORIUM);

                copy(blockProvider, itemProvider, Blocks.SMALL_ZYCHORIUM_BRICKS, Items.SMALL_ZYCHORIUM_BRICKS);
                copy(blockProvider, itemProvider, Blocks.SMALL_BRICKS_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM, Items.SMALL_BRICKS_SOLID_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM);
                copy(blockProvider, itemProvider, Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM, Items.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

                copy(blockProvider, itemProvider, Blocks.ZYCHORIUM_PLATE, Items.ZYCHORIUM_PLATE);
                copy(blockProvider, itemProvider, Blocks.ZYCHORIUM_SHIELD, Items.ZYCHORIUM_SHIELD);

                copy(blockProvider, itemProvider, Blocks.ENGINEERING_BLOCK, Items.ENGINEERING_BLOCK);
                copy(blockProvider, itemProvider, Blocks.ZYCHORIZED_ENGINEERING_BLOCK, Items.ZYCHORIZED_ENGINEERING_BLOCK);
                copy(blockProvider, itemProvider, Blocks.ALUMINIZED_ENGINEERING_BLOCK, Items.ALUMINIZED_ENGINEERING_BLOCK);

                copy(blockProvider, itemProvider, Blocks.COLORABLE, Items.COLORABLE);
                copy(blockProvider, itemProvider, Blocks.ZYCHORIUM_LAMPS, Items.ZYCHORIUM_LAMPS);

                copy(blockProvider, itemProvider, Blocks.VIEWERS, Items.VIEWERS);

                copy(blockProvider, itemProvider, Blocks.BASE_VIEWERS, Items.BASE_VIEWERS);
                copy(blockProvider, itemProvider, Blocks.VIEWERS_BASE, Items.VIEWERS_BASE);
                copy(blockProvider, itemProvider, Blocks.VIEWERS_PHANTOMIZED, Items.VIEWERS_PHANTOMIZED);

                copy(blockProvider, itemProvider, Blocks.IMMORTAL_VIEWERS, Items.IMMORTAL_VIEWERS);
                copy(blockProvider, itemProvider, Blocks.VIEWERS_IMMORTAL, Items.VIEWERS_IMMORTAL);
                copy(blockProvider, itemProvider, Blocks.VIEWERS_PHANTOMIZED_IMMORTAL, Items.VIEWERS_PHANTOMIZED_IMMORTAL);

                itemProvider.getOrCreateBuilder(Tags.Items.GEMS).addTag(Items.ZYCHORIUM);
            });
        });
    }

    private static void copy(RegistrateTagsProvider<Block> blockProvider, RegistrateTagsProvider<Item> itemProvider, ITag.INamedTag<Block> blockTag, ITag.INamedTag<Item> itemTag)
    {
        ITag.Builder itemTagBuilder = itemProvider.getOrCreateBuilder(itemTag).getInternalBuilder();
        ITag.Builder blockTagBuilder = blockProvider.getOrCreateBuilder(blockTag).getInternalBuilder();
        blockTagBuilder.getProxyStream().forEach(itemTagBuilder::addProxyTag);
    }
}
