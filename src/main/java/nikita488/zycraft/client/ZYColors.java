package nikita488.zycraft.client;

import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.tile.ColorableTile;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

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
        else if (tintIndex == 1 && coloredOverlay)
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

    public static IItemColor zyLampItemColor(boolean inverted)
    {
        return (stack, tintIndex) -> !inverted && tintIndex == 0 ? 0x3F3F3F : 0xFFFFFF;
    }

    public static IItemColor colorScannerColor()
    {
        return (stack, tintIndex) ->
        {
            if (!stack.hasTag() || tintIndex != 0)
                return 0xFFFFFF;
            return stack.getTag().getInt("Color");
        };
    }
}
