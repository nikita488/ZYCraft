package nikita488.zycraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.tile.FluidSelectorTile;

import java.util.Map;
import java.util.function.Supplier;

public enum ZYBlockColors implements Supplier<IBlockColor>
{
    COLORABLE((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return 0xFFFFFF;

        TileEntity tile = world.getBlockEntity(pos);
        return tile instanceof IColorable ? ((IColorable)tile).getColor(state, world, pos, tintIndex) : 0xFFFFFF;
    }),
    FABRICATOR((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return ZYType.BLUE.rgb();

        TileEntity tile = world.getBlockEntity(pos);
        return tile instanceof FabricatorTile ? ((FabricatorTile)tile).getColor(state) : ZYType.BLUE.rgb();
    }),
    CONVERTED_MULTI_CHILD((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null)
            return 0xFFFFFF;

        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof ConvertedMultiChildTile)
            return Minecraft.getInstance().getBlockColors().getColor(((ConvertedMultiChildTile)tile).initialState(), world, pos, tintIndex);

        return 0xFFFFFF;
    }),
    VALVE((state, world, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ValveBlock.IO_MODE).rgb() : ZYType.BLUE.rgb()),
    ITEM_IO((state, world, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ItemIOBlock.IO_MODE).rgb() : ZYType.GREEN.rgb()),
    FLUID_SELECTOR((state, world, pos, tintIndex) ->
    {
        if (world == null || pos == null || !state.hasTileEntity())
            return 0xFFFFFF;

        TileEntity tile = world.getBlockEntity(pos);
        return tile instanceof FluidSelectorTile ? ((FluidSelectorTile)tile).getColor() : 0xFFFFFF;
    });

    public static final Map<ZYType, IBlockColor> ZY_COLORS = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, IBlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, world, pos, tintIndex) -> type.rgb());

        return builder.build();
    });
    public static final Map<ZYType, IBlockColor> ZY_COLORS_WITH_OVERLAY = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, IBlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, world, pos, tintIndex) -> tintIndex == 1 ? type.overlayRGB() : type.rgb());

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

    @Override
    public IBlockColor get()
    {
        return color;
    }
}
