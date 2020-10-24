package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.TileEntityEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ViewerType;
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

    public static void init() {}
}
