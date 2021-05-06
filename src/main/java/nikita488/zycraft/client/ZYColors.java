package nikita488.zycraft.client;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.block.state.properties.ValveIOMode;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.util.ItemStackUtils;

public class ZYColors
{
    public static IBlockColor zyBlockColor(ZYType type, boolean coloredOverlay)
    {
        return (state, world, pos, tintIndex) -> zyBase(type, tintIndex, coloredOverlay);
    }

    public static IBlockColor colorableBlockColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (world == null || pos == null || !state.hasTileEntity())
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);
            return tile instanceof IColorable ? ((IColorable)tile).getColor(state, world, pos, tintIndex) : 0xFFFFFF;
        };
    }

    public static IBlockColor fabricatorBlockColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (world == null || pos == null || !state.hasTileEntity())
                return ZYType.BLUE.rgb();

            TileEntity tile = world.getTileEntity(pos);
            return tile instanceof FabricatorTile ? ((FabricatorTile)tile).getColor(state) : ZYType.BLUE.rgb();
        };
    }

    public static IBlockColor valveBlockColor()
    {
        return (state, world, pos, tintIndex) -> tintIndex == 1 ? state.get(ValveBlock.IO_MODE).rgb() : ZYType.BLUE.rgb();
    }

    public static IBlockColor itemIOBlockColor()
    {
        return (state, world, pos, tintIndex) -> tintIndex == 1 ? state.get(ItemIOBlock.IO_MODE).rgb() : ZYType.GREEN.rgb();
    }

    public static IItemColor zyItemColor(ZYType type, boolean coloredOverlay)
    {
        return (stack, tintIndex) -> zyBase(type, tintIndex, coloredOverlay);
    }

    public static IItemColor zychoriumLampItemColor(boolean inverted)
    {
        return (stack, tintIndex) -> inverted ? 0xFFFFFF : 0x3F3F3F;
    }

    public static IItemColor fabricatorItemColor()
    {
        return (stack, tintIndex) -> ZYType.BLUE.rgb();
    }

    public static IItemColor valveItemColor()
    {
        return (stack, tintIndex) -> tintIndex == 1 ? ValveIOMode.IN.rgb() : ZYType.BLUE.rgb();
    }

    public static IItemColor itemIOItemColor()
    {
        return (stack, tintIndex) -> tintIndex == 1 ? ItemIOMode.ALL.rgb() : ZYType.GREEN.rgb();
    }

    public static IItemColor zychoriumItemColor(ZYType type)
    {
        return (stack, tintIndex) -> type.rgb();
    }

    public static IItemColor colorScannerColor()
    {
        return (stack, tintIndex) -> ItemStackUtils.getInt(stack, "Color", 0xFFFFFF);
    }

    public static IItemColor aluminiumFoilColor()
    {
        return (stack, tintIndex) -> tintIndex == 1 ? FluidUtil.getFluidContained(stack)
                .map(containedFluid -> containedFluid.getFluid().getAttributes().getColor(containedFluid))
                .orElse(0xFFFFFF) : ZYType.GREEN.rgb();
    }

    private static int zyBase(ZYType type, int tintIndex, boolean coloredOverlay)
    {
        return tintIndex == 1 && coloredOverlay ? type.overlayRGB() : type.rgb();
    }
}
