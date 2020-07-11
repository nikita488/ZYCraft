package com.nikita488.zycraft.item;

import com.nikita488.zycraft.block.ColorableBlock;
import com.nikita488.zycraft.enums.ZyType;
import com.nikita488.zycraft.tile.ColorableTile;
import com.nikita488.zycraft.util.Color4b;
import com.nikita488.zycraft.util.IColorChanger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;

public class ZychoriumItem extends ZyItem implements IColorChanger
{
    public ZychoriumItem(ZyType type, Properties properties)
    {
        super(type, properties);
    }

    @Override
    public boolean canChangeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        Color4b rgba = colorable.color();
        boolean sneaking = player.isCrouching();

        switch (type)
        {
            case RED:
                return canChangeComponent(rgba.r(), sneaking);
            case GREEN:
                return canChangeComponent(rgba.g(), sneaking);
            case BLUE:
                return canChangeComponent(rgba.b(), sneaking);
        }

        return false;
    }

    private boolean canChangeComponent(int value, boolean sneaking)
    {
        return sneaking ? value != 0 : value != 255;
    }

    @Override
    public void changeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        switch (type)
        {
            case RED:
                if (!player.isCrouching())
                    colorable.addRGB(8, 0, 0);
                else
                    colorable.subtractRGB(8, 0, 0);
                break;
            case GREEN:
                if (!player.isCrouching())
                    colorable.addRGB(0, 8, 0);
                else
                    colorable.subtractRGB(0, 8, 0);
                break;
            case BLUE:
                if (!player.isCrouching())
                    colorable.addRGB(0, 0, 8);
                else
                    colorable.subtractRGB(0, 0, 8);
                break;
        }
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return world.getBlockState(pos).getBlock() instanceof ColorableBlock;
    }
}
