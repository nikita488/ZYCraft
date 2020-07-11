package com.nikita488.zycraft.util;

import com.nikita488.zycraft.tile.ColorableTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

public interface IColorChanger
{
    boolean canChangeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit);

    void changeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit);
}
