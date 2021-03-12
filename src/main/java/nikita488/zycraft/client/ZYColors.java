package nikita488.zycraft.client;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.enums.ZYType;
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

            TileEntity tile = world.getBlockEntity(pos);

            if (!(tile instanceof ColorableTile))
                return 0xFFFFFF;

            return ((ColorableBlock)state.getBlock()).getColor(state, (ColorableTile)tile, tintIndex);
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
}
