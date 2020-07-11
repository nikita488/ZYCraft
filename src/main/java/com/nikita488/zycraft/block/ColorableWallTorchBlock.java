package com.nikita488.zycraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ColorableWallTorchBlock extends WallTorchBlock
{
    public ColorableWallTorchBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public String getTranslationKey()
    {
        return "";
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {

    }
}
