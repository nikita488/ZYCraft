package nikita488.zycraft.init;

import com.google.common.collect.Lists;
import nikita488.zycraft.ZYCraft;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ZYTags
{
    public static class Blocks
    {
        public static final Tag<Block> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final Tag<Block> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final Tag<Block> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final Tag<Block> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final Tag<Block> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final Tag<Block> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final Tag<Block> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final Tag<Block> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final Tag<Block> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final Tag<Block> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final Tag<Block> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final Tag<Block> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final Tag<Block> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final Tag<Block> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final Tag<Block> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final Tag<Block> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final Tag<Block> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final Tag<Block> ENGINEERING_BLOCK = tag("engineering_block");
        public static final Tag<Block> ZYCHORIZED_ENGINEERING_BLOCK = tag("zychorized_engineering_block");
        public static final Tag<Block> ALUMINIZED_ENGINEERING_BLOCK = tag("aluminized_engineering_block");

        public static final Tag<Block> COLORABLE = tag("colorable");
        public static final Tag<Block> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final Tag<Block> VIEWERS = tag("viewers");

        public static final Tag<Block> BASE_VIEWERS = tag("base_viewers");
        public static final Tag<Block> VIEWERS_BASE = tag("viewers/base");
        public static final Tag<Block> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final Tag<Block> IMMORTAL_VIEWERS = tag("immortal_viewers");
        public static final Tag<Block> VIEWERS_IMMORTAL = tag("viewers/immortal");
        public static final Tag<Block> VIEWERS_PHANTOMIZED_IMMORTAL = tag("viewers/phantomized_immortal");

        public static final Tag<Block> BASIC_MACHINES = tag("basic_machines");

        private static Tag<Block> tag(String name)
        {
            return new BlockTags.Wrapper(ZYCraft.modLoc(name));
        }

        private static Tag<Block> forgeTag(String name)
        {
            return new BlockTags.Wrapper(new ResourceLocation("forge", name));
        }
    }

    public static class Items
    {
        public static final Tag<Item> ORES_ZYCHORIUM = tag("ores/zychorium");
        public static final Tag<Item> ORES_ALUMINIUM = forgeTag("ores/aluminium");
        public static final Tag<Item> ORES_ALUMINUM = forgeTag("ores/aluminum");

        public static final Tag<Item> STORAGE_BLOCKS_ZYCHORIUM = tag("storage_blocks/zychorium");

        public static final Tag<Item> ZYCHORIUM_BRICKS = tag("zychorium_bricks");

        public static final Tag<Item> BASE_ZYCHORIUM_BRICKS = tag("base_zychorium_bricks");
        public static final Tag<Item> BRICKS_ZYCHORIUM = tag("bricks/zychorium");
        public static final Tag<Item> BRICKS_SOLID_ZYCHORIUM = tag("bricks/solid_zychorium");
        public static final Tag<Item> BRICKS_ZYCHORIZED_ZYCHORIUM = tag("bricks/zychorized_zychorium");
        public static final Tag<Item> BRICKS_ALUMINIZED_ZYCHORIUM = tag("bricks/aluminized_zychorium");

        public static final Tag<Item> SMALL_ZYCHORIUM_BRICKS = tag("small_zychorium_bricks");
        public static final Tag<Item> SMALL_BRICKS_ZYCHORIUM = tag("small_bricks/zychorium");
        public static final Tag<Item> SMALL_BRICKS_SOLID_ZYCHORIUM = tag("small_bricks/solid_zychorium");
        public static final Tag<Item> SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM = tag("small_bricks/zychorized_zychorium");
        public static final Tag<Item> SMALL_BRICKS_ALUMINIZED_ZYCHORIUM = tag("small_bricks/aluminized_zychorium");

        public static final Tag<Item> ZYCHORIUM_PLATE = tag("zychorium_plate");
        public static final Tag<Item> ZYCHORIUM_SHIELD = tag("zychorium_shield");

        public static final Tag<Item> ENGINEERING_BLOCK = tag("engineering_block");
        public static final Tag<Item> ZYCHORIZED_ENGINEERING_BLOCK = tag("zychorized_engineering_block");
        public static final Tag<Item> ALUMINIZED_ENGINEERING_BLOCK = tag("aluminized_engineering_block");

        public static final Tag<Item> COLORABLE = tag("colorable");
        public static final Tag<Item> ZYCHORIUM_LAMPS = tag("zychorium_lamps");

        public static final Tag<Item> VIEWERS = tag("viewers");

        public static final Tag<Item> BASE_VIEWERS = tag("base_viewers");
        public static final Tag<Item> VIEWERS_BASE = tag("viewers/base");
        public static final Tag<Item> VIEWERS_PHANTOMIZED = tag("viewers/phantomized");

        public static final Tag<Item> IMMORTAL_VIEWERS = tag("immortal_viewers");
        public static final Tag<Item> VIEWERS_IMMORTAL = tag("viewers/immortal");
        public static final Tag<Item> VIEWERS_PHANTOMIZED_IMMORTAL = tag("viewers/phantomized_immortal");

        public static final Tag<Item> BASIC_MACHINES = tag("basic_machines");

        public static final Tag<Item> ZYCHORIUM = tag("zychorium");

        private static Tag<Item> tag(String name)
        {
            return new ItemTags.Wrapper(ZYCraft.modLoc(name));
        }

        private static Tag<Item> forgeTag(String name)
        {
            return new ItemTags.Wrapper(new ResourceLocation("forge", name));
        }
    }

    public static void init()
    {
        ZYCraft.REGISTRY.addDataGenerator(ProviderType.BLOCK_TAGS, provider ->
        {
            provider.getBuilder(Tags.Blocks.ORES)
                    .add(Blocks.ORES_ZYCHORIUM, Blocks.ORES_ALUMINIUM, Blocks.ORES_ALUMINUM);

            provider.getBuilder(Tags.Blocks.STORAGE_BLOCKS)
                    .add(Blocks.STORAGE_BLOCKS_ZYCHORIUM);

            provider.getBuilder(Blocks.BASE_ZYCHORIUM_BRICKS)
                    .add(Blocks.BRICKS_ZYCHORIUM,
                            Blocks.BRICKS_SOLID_ZYCHORIUM,
                            Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM,
                            Blocks.BRICKS_ALUMINIZED_ZYCHORIUM);

            provider.getBuilder(Blocks.SMALL_ZYCHORIUM_BRICKS)
                    .add(Blocks.SMALL_BRICKS_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM,
                            Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

            provider.getBuilder(Blocks.ZYCHORIUM_BRICKS)
                    .add(Blocks.BASE_ZYCHORIUM_BRICKS, Blocks.SMALL_ZYCHORIUM_BRICKS);

            provider.getBuilder(BlockTags.STONE_BRICKS)
                    .add(Blocks.ZYCHORIUM_BRICKS);

            provider.getBuilder(Blocks.ENGINEERING_BLOCK)
                    .add(Blocks.ZYCHORIZED_ENGINEERING_BLOCK, Blocks.ALUMINIZED_ENGINEERING_BLOCK);

            provider.getBuilder(Blocks.BASE_VIEWERS)
                    .add(Blocks.VIEWERS_BASE, Blocks.VIEWERS_PHANTOMIZED);

            provider.getBuilder(Blocks.IMMORTAL_VIEWERS)
                    .add(Blocks.VIEWERS_IMMORTAL, Blocks.VIEWERS_PHANTOMIZED_IMMORTAL);

            provider.getBuilder(Blocks.VIEWERS)
                    .add(Blocks.BASE_VIEWERS, Blocks.IMMORTAL_VIEWERS);

            provider.getBuilder(Tags.Blocks.GLASS_COLORLESS)
                    .add(Blocks.BASE_VIEWERS);

            provider.getBuilder(Tags.Blocks.STAINED_GLASS)
                    .add(Blocks.IMMORTAL_VIEWERS);

            provider.getBuilder(Blocks.COLORABLE)
                    .add(Blocks.ZYCHORIUM_LAMPS, Blocks.IMMORTAL_VIEWERS);
        });

        ZYCraft.REGISTRY.addDataGenerator(ProviderType.ITEM_TAGS, provider ->
        {
            copy(provider, Blocks.ORES_ZYCHORIUM, Items.ORES_ZYCHORIUM);
            copy(provider, Blocks.ORES_ALUMINIUM, Items.ORES_ALUMINIUM);
            copy(provider, Blocks.ORES_ALUMINUM, Items.ORES_ALUMINUM);

            copy(provider, Blocks.STORAGE_BLOCKS_ZYCHORIUM, Items.STORAGE_BLOCKS_ZYCHORIUM);

            copy(provider, Blocks.ZYCHORIUM_BRICKS, Items.ZYCHORIUM_BRICKS);

            copy(provider, Blocks.BASE_ZYCHORIUM_BRICKS, Items.BASE_ZYCHORIUM_BRICKS);
            copy(provider, Blocks.BRICKS_ZYCHORIUM, Items.BRICKS_ZYCHORIUM);
            copy(provider, Blocks.BRICKS_SOLID_ZYCHORIUM, Items.BRICKS_SOLID_ZYCHORIUM);
            copy(provider, Blocks.BRICKS_ZYCHORIZED_ZYCHORIUM, Items.BRICKS_ZYCHORIZED_ZYCHORIUM);
            copy(provider, Blocks.BRICKS_ALUMINIZED_ZYCHORIUM, Items.BRICKS_ALUMINIZED_ZYCHORIUM);

            copy(provider, Blocks.SMALL_ZYCHORIUM_BRICKS, Items.SMALL_ZYCHORIUM_BRICKS);
            copy(provider, Blocks.SMALL_BRICKS_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIUM);
            copy(provider, Blocks.SMALL_BRICKS_SOLID_ZYCHORIUM, Items.SMALL_BRICKS_SOLID_ZYCHORIUM);
            copy(provider, Blocks.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM, Items.SMALL_BRICKS_ZYCHORIZED_ZYCHORIUM);
            copy(provider, Blocks.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM, Items.SMALL_BRICKS_ALUMINIZED_ZYCHORIUM);

            copy(provider, Blocks.ZYCHORIUM_PLATE, Items.ZYCHORIUM_PLATE);
            copy(provider, Blocks.ZYCHORIUM_SHIELD, Items.ZYCHORIUM_SHIELD);

            copy(provider, Blocks.ENGINEERING_BLOCK, Items.ENGINEERING_BLOCK);
            copy(provider, Blocks.ZYCHORIZED_ENGINEERING_BLOCK, Items.ZYCHORIZED_ENGINEERING_BLOCK);
            copy(provider, Blocks.ALUMINIZED_ENGINEERING_BLOCK, Items.ALUMINIZED_ENGINEERING_BLOCK);

            copy(provider, Blocks.COLORABLE, Items.COLORABLE);
            copy(provider, Blocks.ZYCHORIUM_LAMPS, Items.ZYCHORIUM_LAMPS);

            copy(provider, Blocks.VIEWERS, Items.VIEWERS);

            copy(provider, Blocks.BASE_VIEWERS, Items.BASE_VIEWERS);
            copy(provider, Blocks.VIEWERS_BASE, Items.VIEWERS_BASE);
            copy(provider, Blocks.VIEWERS_PHANTOMIZED, Items.VIEWERS_PHANTOMIZED);

            copy(provider, Blocks.IMMORTAL_VIEWERS, Items.IMMORTAL_VIEWERS);
            copy(provider, Blocks.VIEWERS_IMMORTAL, Items.VIEWERS_IMMORTAL);
            copy(provider, Blocks.VIEWERS_PHANTOMIZED_IMMORTAL, Items.VIEWERS_PHANTOMIZED_IMMORTAL);

            provider.getBuilder(Tags.Items.GEMS).add(Items.ZYCHORIUM);
        });
    }

    public static void copy(RegistrateTagsProvider<Item> provider, Tag<Block> from, Tag<Item> to)
    {
        Tag.Builder<Item> builder = provider.getBuilder(to);

        for(Tag.ITagEntry<Block> blockEntry : from.getEntries())
            builder.add(copyEntry(blockEntry));
    }

    private static Tag.ITagEntry<Item> copyEntry(Tag.ITagEntry<Block> entry)
    {
        if (entry instanceof Tag.TagEntry)
        {
            return new Tag.TagEntry<>(((Tag.TagEntry)entry).getSerializedId());
        }
        else if (entry instanceof Tag.ListEntry)
        {
            List<Item> list = Lists.newArrayList();

            for (Block block : ((Tag.ListEntry<Block>)entry).getTaggedItems())
            {
                Item item = block.asItem();

                if (item == net.minecraft.item.Items.AIR)
                    ZYCraft.LOGGER.warn("Itemless block copied to item tag: {}", ForgeRegistries.BLOCKS.getKey(block));
                else
                    list.add(item);
            }

            return new Tag.ListEntry<>(list);
        }
        else
        {
            throw new UnsupportedOperationException("Unknown tag entry " + entry);
        }
    }
}
