package nikita488.zycraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.tile.FluidSelectorTile;

import java.util.Map;
import java.util.function.Supplier;

public enum ZYBlockColors implements Supplier<BlockColor>
{
    COLORABLE((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return 0xFFFFFF;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof IColorable ? ((IColorable)blockEntity).getColor(state, world, pos, tintIndex) : 0xFFFFFF;
    }),
    FABRICATOR((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return ZYType.BLUE.rgb();

        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof FabricatorTile ? ((FabricatorTile)blockEntity).getColor(state) : ZYType.BLUE.rgb();
    }),
    CONVERTED_MULTI_CHILD((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null)
            return 0xFFFFFF;

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ConvertedMultiChildTile)
            return Minecraft.getInstance().getBlockColors().getColor(((ConvertedMultiChildTile)blockEntity).initialState(), world, pos, tintIndex);

        return 0xFFFFFF;
    }),
    VALVE((state, world, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ValveBlock.IO_MODE).rgb() : ZYType.BLUE.rgb()),
    ITEM_IO((state, world, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ItemIOBlock.IO_MODE).rgb() : ZYType.GREEN.rgb()),
    FLUID_SELECTOR((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return 0xFFFFFF;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof FluidSelectorTile ? ((FluidSelectorTile)blockEntity).getColor() : 0xFFFFFF;
    });

    public static final Map<ZYType, BlockColor> ZY_COLORS = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, BlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, world, pos, tintIndex) -> type.rgb());

        return builder.build();
    });
    public static final Map<ZYType, BlockColor> ZY_COLORS_WITH_OVERLAY = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, BlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, world, pos, tintIndex) -> tintIndex == 1 ? type.overlayRGB() : type.rgb());

        return builder.build();
    });
    private final BlockColor color;

    ZYBlockColors(BlockColor color)
    {
        this.color = color;
    }

    public static BlockColor getZYBlockColor(ZYType type)
    {
        return getZYBlockColor(type, false);
    }

    public static BlockColor getZYBlockColor(ZYType type, boolean coloredOverlay)
    {
        return (coloredOverlay ? ZY_COLORS_WITH_OVERLAY : ZY_COLORS).get(type);
    }

    @Override
    public BlockColor get()
    {
        return color;
    }
}
