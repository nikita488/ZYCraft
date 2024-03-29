package nikita488.zycraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.tile.FluidSelectorTile;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public enum ZYBlockColors implements Supplier<IBlockColor>
{
    COLORABLE((state, getter, pos, tintIndex) ->
    {
        TileEntity blockEntity = getBlockEntity(state, getter, pos);
        return blockEntity instanceof IColorable ? ((IColorable)blockEntity).getColor(state, getter, pos, tintIndex) : 0xFFFFFF;
    }),
    FABRICATOR((state, getter, pos, tintIndex) ->
    {
        TileEntity blockEntity = getBlockEntity(state, getter, pos);
        return blockEntity instanceof FabricatorTile ? ((FabricatorTile)blockEntity).getColor(state) : ZYType.BLUE.rgb();
    }),
    CONVERTED_MULTI_CHILD((state, getter, pos, tintIndex) ->
    {
        TileEntity blockEntity = getBlockEntity(state, getter, pos);
        return blockEntity instanceof ConvertedMultiChildTile ? Minecraft.getInstance().getBlockColors().getColor(((ConvertedMultiChildTile)blockEntity).initialState(), getter, pos, tintIndex) : 0xFFFFFF;
    }),
    VALVE((state, getter, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ValveBlock.IO_MODE).rgb() : ZYType.BLUE.rgb()),
    ITEM_IO((state, getter, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ItemIOBlock.IO_MODE).rgb() : ZYType.GREEN.rgb()),
    FLUID_SELECTOR((state, getter, pos, tintIndex) ->
    {
        TileEntity blockEntity = getBlockEntity(state, getter, pos);
        return blockEntity instanceof FluidSelectorTile ? ((FluidSelectorTile)blockEntity).getColor() : 0xFFFFFF;
    });

    public static final Map<ZYType, IBlockColor> ZY_COLORS = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, IBlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, getter, pos, tintIndex) -> type.rgb());

        return builder.build();
    });
    public static final Map<ZYType, IBlockColor> ZY_COLORS_WITH_OVERLAY = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, IBlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, getter, pos, tintIndex) -> tintIndex == 1 ? type.overlayRGB() : type.rgb());

        return builder.build();
    });
    private final IBlockColor color;

    ZYBlockColors(IBlockColor color)
    {
        this.color = color;
    }

    public static IBlockColor getZYBlockColor(ZYType type)
    {
        return getZYBlockColor(type, false);
    }

    public static IBlockColor getZYBlockColor(ZYType type, boolean coloredOverlay)
    {
        return (coloredOverlay ? ZY_COLORS_WITH_OVERLAY : ZY_COLORS).get(type);
    }

    @Nullable
    private static TileEntity getBlockEntity(BlockState state, @Nullable IBlockDisplayReader getter, @Nullable BlockPos pos)
    {
        return getter != null && pos != null && state.hasTileEntity() ? getter.getBlockEntity(pos) : null;
    }

    @Override
    public IBlockColor get()
    {
        return color;
    }
}
