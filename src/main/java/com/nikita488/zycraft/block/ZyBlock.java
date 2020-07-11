package com.nikita488.zycraft.block;

import com.nikita488.zycraft.enums.ZyType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ZyBlock extends Block
{
    protected final ZyType type;

    public ZyBlock(ZyType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    @Override
    public MaterialColor getMaterialColor(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type.mtlColor();
    }

    public ZyType type()
    {
        return type;
    }
}
