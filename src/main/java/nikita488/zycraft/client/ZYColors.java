package nikita488.zycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.io.IOType;
import nikita488.zycraft.multiblock.tile.DefaultMultiChildTile;
import nikita488.zycraft.multiblock.tile.ItemIOTile;
import nikita488.zycraft.multiblock.tile.ValveTile;
import nikita488.zycraft.tile.ColorableTile;
import nikita488.zycraft.util.ItemStackUtils;

public class ZYColors
{
    public static IBlockColor zyBlockColor(ZYType type, boolean coloredOverlay)
    {
        return (state, world, pos, tintIndex) -> zyRGB(type, tintIndex, coloredOverlay);
    }

    public static IItemColor zyItemColor(ZYType type, boolean coloredOverlay)
    {
        return (stack, tintIndex) -> zyRGB(type, tintIndex, coloredOverlay);
    }

    private static int zyRGB(ZYType type, int tintIndex, boolean coloredOverlay)
    {
        if (tintIndex == 0)
            return type.rgb();
        if (tintIndex == 1 && coloredOverlay)
            return type.darkRGB();
        return 0xFFFFFF;
    }

    public static IBlockColor colorableBlockColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (!(state.getBlock() instanceof ColorableBlock) && world == null || pos == null)
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);

            if (!(tile instanceof ColorableTile))
                return 0xFFFFFF;

            return ((ColorableBlock)state.getBlock()).getColor(state, (ColorableTile)tile, tintIndex);
        };
    }

    public static IBlockColor valveBlockColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (world == null || pos == null)
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);

            if (!(tile instanceof ValveTile))
                return 0xFFFFFF;

            if (tintIndex == 0)
                return ZYType.BLUE.rgb();
            if (tintIndex == 1)
                return ((ValveTile)tile).getIO().rgb();
            return 0xFFFFFF;
        };
    }

    public static IBlockColor itemIOBlockColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (world == null || pos == null)
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);

            if (!(tile instanceof ItemIOTile))
                return 0xFFFFFF;

            if (tintIndex == 0)
                return ZYType.GREEN.rgb();;
            if (tintIndex == 1)
                return ((ItemIOTile)tile).getIO().rgb();
            return 0xFFFFFF;
        };
    }

    public static IBlockColor defaultMultiChildColor()
    {
        return (state, world, pos, tintIndex) ->
        {
            if (world == null || pos == null)
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof DefaultMultiChildTile)
                return Minecraft.getInstance().getBlockColors().getColor(((DefaultMultiChildTile)tile).state(), world, pos, tintIndex);

            return 0xFFFFFF;
        };
    }

    public static IItemColor zyLampItemColor(boolean inverted)
    {
        return (stack, tintIndex) -> !inverted && tintIndex == 0 ? 0x3F3F3F : 0xFFFFFF;
    }

    public static IItemColor colorScannerColor()
    {
        return (stack, tintIndex) -> tintIndex == 0 ? ItemStackUtils.getInt(stack, "Color", 0xFFFFFF) : 0xFFFFFF;
    }

    public static IItemColor aluminiumFoilColor()
    {
        return (stack, tintIndex) ->
        {
            if (tintIndex == 0)
                return ZYType.GREEN.rgb();
            if (tintIndex == 1)
                return FluidUtil.getFluidContained(stack)
                        .map(containedFluid -> containedFluid.getFluid().getAttributes().getColor(containedFluid))
                        .orElse(0xFFFFFF);
            return 0xFFFFFF;
        };
    }

    public static IItemColor valveItemColor()
    {
        return (stack, tintIndex) ->
        {
            if (tintIndex == 0)
                return ZYType.BLUE.rgb();
            if (tintIndex == 1)
                return IOType.ALL_IN.rgb();
            return 0xFFFFFF;
        };
    }

    public static IItemColor itemIOItemColor()
    {
        return (stack, tintIndex) -> tintIndex == 0 ? ZYType.GREEN.rgb() : IOType.ALL.rgb();
    }
}
