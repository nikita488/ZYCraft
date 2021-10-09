package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import nikita488.zycraft.multiblock.child.tile.ItemIOTile;
import nikita488.zycraft.multiblock.child.tile.MultiAirTile;
import nikita488.zycraft.multiblock.child.tile.ValveTile;
import nikita488.zycraft.tile.ColorableTile;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.tile.FluidSelectorTile;
import nikita488.zycraft.tile.ZychoriumLampTile;

public class ZYTiles
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final TileEntityEntry<ColorableTile> COLORABLE = REGISTRATE.<ColorableTile>tileEntity("colorable", ColorableTile::new)
            .validBlocks(
                    ZYBlocks.IMMORTAL_BLOCK,
                    ZYBlocks.THE_AUREY_BLOCK,
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.BASIC),
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.GLOWING),
                    ZYBlocks.IMMORTAL_VIEWER.get(ViewerType.DARK),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.BASIC),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.GLOWING),
                    ZYBlocks.PHANTOMIZED_IMMORTAL_VIEWER.get(ViewerType.DARK))
            .register();

    public static final TileEntityEntry<ZychoriumLampTile> ZYCHORIUM_LAMP = REGISTRATE.tileEntity("zychorium_lamp", ZychoriumLampTile::new)
            .validBlocks(ZYBlocks.ZYCHORIUM_LAMP, ZYBlocks.INVERTED_ZYCHORIUM_LAMP)
            .register();

    public static final TileEntityEntry<FabricatorTile> FABRICATOR = TileEntityEntry.cast(ZYBlocks.FABRICATOR.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static final TileEntityEntry<MultiAirTile> MULTI_AIR = TileEntityEntry.cast(ZYBlocks.MULTI_AIR.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<ConvertedMultiChildTile> CONVERTED_MULTI_CHILD = REGISTRATE.tileEntity("converted_multi_child", ConvertedMultiChildTile::new)
            .validBlocks(ZYBlocks.FLAMMABLE_BLOCK, ZYBlocks.HARD_BLOCK, ZYBlocks.GLASS_BLOCK)
            .register();
    public static final TileEntityEntry<ValveTile> VALVE = TileEntityEntry.cast(ZYBlocks.VALVE.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<ItemIOTile> ITEM_IO = TileEntityEntry.cast(ZYBlocks.ITEM_IO.getSibling(ForgeRegistries.TILE_ENTITIES));
    public static final TileEntityEntry<FluidSelectorTile> FLUID_SELECTOR = TileEntityEntry.cast(ZYBlocks.FLUID_SELECTOR.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static void init() {}
}
