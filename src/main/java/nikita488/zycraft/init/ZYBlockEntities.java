package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.TileEntityEntry;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.entity.ColorableBlockEntity;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.block.entity.FluidSelectorBlockEntity;
import nikita488.zycraft.block.entity.ZychoriumLampBlockEntity;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.multiblock.child.block.entity.ConvertedMultiChildBlockEntity;
import nikita488.zycraft.multiblock.child.block.entity.ItemIOBlockEntity;
import nikita488.zycraft.multiblock.child.block.entity.MultiAirBlockEntity;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;

public class ZYBlockEntities
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final TileEntityEntry<ColorableBlockEntity> COLORABLE = REGISTRATE.<ColorableBlockEntity>tileEntity("colorable", (pos, state, type) -> new ColorableBlockEntity(type, pos, state))
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

    public static final TileEntityEntry<ZychoriumLampBlockEntity> ZYCHORIUM_LAMP = REGISTRATE.<ZychoriumLampBlockEntity>tileEntity("zychorium_lamp", (pos, state, type) -> new ZychoriumLampBlockEntity(type, pos, state))
            .validBlocks(ZYBlocks.ZYCHORIUM_LAMP, ZYBlocks.INVERTED_ZYCHORIUM_LAMP)
            .register();

    public static final TileEntityEntry<FabricatorBlockEntity> FABRICATOR = TileEntityEntry.cast(ZYBlocks.FABRICATOR.getSibling(ForgeRegistries.BLOCK_ENTITIES));

    public static final TileEntityEntry<MultiAirBlockEntity> MULTI_AIR = TileEntityEntry.cast(ZYBlocks.MULTI_AIR.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final TileEntityEntry<ConvertedMultiChildBlockEntity> CONVERTED_MULTI_CHILD = REGISTRATE.<ConvertedMultiChildBlockEntity>tileEntity("converted_multi_child", (pos, state, type) -> new ConvertedMultiChildBlockEntity(type, pos, state))
            .validBlocks(ZYBlocks.FLAMMABLE_BLOCK, ZYBlocks.HARD_BLOCK, ZYBlocks.GLASS_BLOCK)
            .register();
    public static final TileEntityEntry<ValveBlockEntity> VALVE = TileEntityEntry.cast(ZYBlocks.VALVE.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final TileEntityEntry<ItemIOBlockEntity> ITEM_IO = TileEntityEntry.cast(ZYBlocks.ITEM_IO.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final TileEntityEntry<FluidSelectorBlockEntity> FLUID_SELECTOR = TileEntityEntry.cast(ZYBlocks.FLUID_SELECTOR.getSibling(ForgeRegistries.BLOCK_ENTITIES));

    public static void init() {}
}
