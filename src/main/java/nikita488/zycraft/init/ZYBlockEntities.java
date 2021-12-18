package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
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

    public static final BlockEntityEntry<ColorableBlockEntity> COLORABLE = REGISTRATE.<ColorableBlockEntity>blockEntity("colorable", ColorableBlockEntity::new)
            .validBlocks(ZYBlocks.PAINTABLE_BLOCK,
                    ZYBlocks.SOLID_PAINTABLE_BLOCK,
                    ZYBlocks.PAINTABLE_VIEWER.get(ViewerType.BASIC),
                    ZYBlocks.PAINTABLE_VIEWER.get(ViewerType.GLOWING),
                    ZYBlocks.PAINTABLE_VIEWER.get(ViewerType.DARK),
                    ZYBlocks.PHANTOMIZED_PAINTABLE_VIEWER.get(ViewerType.BASIC),
                    ZYBlocks.PHANTOMIZED_PAINTABLE_VIEWER.get(ViewerType.GLOWING),
                    ZYBlocks.PHANTOMIZED_PAINTABLE_VIEWER.get(ViewerType.DARK))
            .register();

    public static final BlockEntityEntry<ZychoriumLampBlockEntity> ZYCHORIUM_LAMP = REGISTRATE.blockEntity("zychorium_lamp", ZychoriumLampBlockEntity::new)
            .validBlocks(ZYBlocks.ZYCHORIUM_LAMP, ZYBlocks.INVERTED_ZYCHORIUM_LAMP)
            .register();

    public static final BlockEntityEntry<FabricatorBlockEntity> FABRICATOR = BlockEntityEntry.cast(ZYBlocks.FABRICATOR.getSibling(ForgeRegistries.BLOCK_ENTITIES));

    public static final BlockEntityEntry<MultiAirBlockEntity> MULTI_AIR = BlockEntityEntry.cast(ZYBlocks.MULTI_AIR.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final BlockEntityEntry<ConvertedMultiChildBlockEntity> CONVERTED_MULTI_CHILD = REGISTRATE.blockEntity("converted_multi_child", ConvertedMultiChildBlockEntity::new)
            .validBlocks(ZYBlocks.FLAMMABLE_BLOCK, ZYBlocks.HARD_BLOCK, ZYBlocks.GLASS_BLOCK)
            .register();
    public static final BlockEntityEntry<ValveBlockEntity> VALVE = BlockEntityEntry.cast(ZYBlocks.VALVE.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final BlockEntityEntry<ItemIOBlockEntity> ITEM_IO = BlockEntityEntry.cast(ZYBlocks.ITEM_IO.getSibling(ForgeRegistries.BLOCK_ENTITIES));
    public static final BlockEntityEntry<FluidSelectorBlockEntity> FLUID_SELECTOR = BlockEntityEntry.cast(ZYBlocks.FLUID_SELECTOR.getSibling(ForgeRegistries.BLOCK_ENTITIES));

    public static void init() {}
}
