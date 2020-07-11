package com.nikita488.zycraft.init;

import com.nikita488.zycraft.ZYCraft;
import com.nikita488.zycraft.enums.ViewerType;
import com.nikita488.zycraft.tile.ColorableTile;
import com.tterrag.registrate.util.entry.TileEntityEntry;

public class ModTiles
{
    public static final TileEntityEntry<ColorableTile> COLORABLE = ZYCraft.REGISTRY.object("colorable")
            .tileEntity(ColorableTile::new)
            .validBlocks(ModBlocks.ZYCHORIUM_LAMP,
                    ModBlocks.INVERTED_ZYCHORIUM_LAMP,
                    ModBlocks.IMMORTAL_BLOCK,
                    ModBlocks.THE_AUREY_BLOCK,
                    ModBlocks.IMMORTAL_VIEWER.get(ViewerType.IMMORTAL),
                    ModBlocks.IMMORTAL_VIEWER.get(ViewerType.GLOWING_IMMORTAL),
                    ModBlocks.IMMORTAL_VIEWER.get(ViewerType.DARK_IMMORTAL),
                    ModBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.IMMORTAL),
                    ModBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.GLOWING_IMMORTAL),
                    ModBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.DARK_IMMORTAL))
            .register();

    public static void init() {}
}
