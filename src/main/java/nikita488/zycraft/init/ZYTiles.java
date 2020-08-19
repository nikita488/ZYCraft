package nikita488.zycraft.init;

import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.multiblock.child.DefaultMultiChildTile;
import nikita488.zycraft.api.multiblock.child.MultiChildMaterial;
import nikita488.zycraft.multiblock.test.TestTile;
import nikita488.zycraft.tile.ColorableTile;
import com.tterrag.registrate.util.entry.TileEntityEntry;

public class ZYTiles
{
    public static final TileEntityEntry<ColorableTile> COLORABLE = ZYCraft.REGISTRY.object("colorable")
            .tileEntity(ColorableTile::new)
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
    public static final TileEntityEntry<DefaultMultiChildTile> DEFAULT_MULTI_CHILD = ZYCraft.REGISTRY.object("default_multi_child")
            .tileEntity(DefaultMultiChildTile::new)
            .validBlocks(ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.AIR),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.SOFT),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.HARD),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.GLASS),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.FLAMMABLE),
                    ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildMaterial.NON_OPAQUE))
            .register();
    public static final TileEntityEntry<TestTile> TEST = TileEntityEntry.cast(ZYBlocks.VALVE.getSibling(ForgeRegistries.TILE_ENTITIES));

    public static void init() {}
}
