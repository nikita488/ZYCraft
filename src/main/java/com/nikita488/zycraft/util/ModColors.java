package com.nikita488.zycraft.util;

import com.nikita488.zycraft.block.ColorableBlock;
import com.nikita488.zycraft.enums.ZyType;
import com.nikita488.zycraft.tile.ColorableTile;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class ModColors
{
    public static IBlockColor zyBlockColor(ZyType type, boolean coloredOverlay)
    {
        return (state, world, pos, tintIndex) -> zyRGB(type, tintIndex, coloredOverlay);
    }

    public static IItemColor zyItemColor(ZyType type, boolean coloredOverlay)
    {
        return (stack, tintIndex) -> zyRGB(type, tintIndex, coloredOverlay);
    }

    private static int zyRGB(ZyType type, int tintIndex, boolean coloredOverlay)
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
            if (!(state.getBlock() instanceof ColorableBlock) && world == null || pos == null || tintIndex != 0)
                return 0xFFFFFF;

            TileEntity tile = world.getTileEntity(pos);

            if (!(tile instanceof ColorableTile))
                return 0xFFFFFF;

            return ((ColorableBlock)state.getBlock()).getColor(state, (ColorableTile)tile);
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
            CompoundNBT compound = stack.getTag();
            if (compound == null || tintIndex != 0)
                return 0xFFFFFF;
            return compound.getInt("Color");
        };
    }
}
