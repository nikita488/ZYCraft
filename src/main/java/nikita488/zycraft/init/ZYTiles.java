package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.multiblock.MultiChildType;
import nikita488.zycraft.multiblock.tile.DefaultMultiChildTile;
import nikita488.zycraft.multiblock.tile.ItemIOTile;
import nikita488.zycraft.multiblock.tile.ValveTile;
import nikita488.zycraft.tile.ColorableTile;

public class ZYTiles
{
    public static final TileEntityEntry<ColorableTile> COLORABLE = ZYCraft.registrate().<ColorableTile>tileEntity("colorable", ColorableTile::new)
            .validBlocks(ZYBlocks.ZYCHORIUM_LAMP,
                    ZYBlocks.INVERTED_ZYCHORIUM_LAMP,
                    ZYBlocks.IMMORTAL_BLOCK,
                    ZYBlocks.THE_AUREY_BLOCK,
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.IMMORTAL),
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.GLOWING_IMMORTAL),
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.DARK_IMMORTAL),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.IMMORTAL),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.GLOWING_IMMORTAL),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.DARK_IMMORTAL))
            .register();
    public static final TileEntityEntry<DefaultMultiChildTile> DEFAULT_MULTI_CHILD = ZYCraft.registrate().tileEntity("default_multi_child", DefaultMultiChildTile::new)
            .validBlocks(
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildType.AIR),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildType.HARD),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildType.GLASS))
            .register();
    public static final TileEntityEntry<ValveTile> VALVE = TileEntityEntry.cast(ZYBlocks.VALVE.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<ItemIOTile> ITEM_IO = TileEntityEntry.cast(ZYBlocks.ITEM_IO.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static void init() {}
}
