package com.nikita488.zycraft.util;

import com.nikita488.zycraft.block.QuartzCrystalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class QuartzCrystalShapes {
    public static final VoxelShape[] DOWN = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 8, 6, 10, 16, 10),
                    Block.makeCuboidShape(6, 8, 6, 10, 16, 12),
                    Block.makeCuboidShape(3, 8, 6, 10, 16, 12),
                    Block.makeCuboidShape(3, 8, 6, 13, 16, 12),
                    Block.makeCuboidShape(3, 8, 4, 13, 16, 12)
            };

    public static final VoxelShape[] UP = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 0, 6, 10, 8, 10),
                    Block.makeCuboidShape(6, 0, 4, 10, 8, 10),
                    Block.makeCuboidShape(3, 0, 4, 10, 8, 10),
                    Block.makeCuboidShape(3, 0, 4, 13, 8, 10),
                    Block.makeCuboidShape(3, 0, 4, 13, 8, 12)
            };

    public static final VoxelShape[] SOUTH = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 0, 10, 10, 8),
                    Block.makeCuboidShape(6, 6, 0, 10, 12, 8),
                    Block.makeCuboidShape(3, 6, 0, 10, 12, 8),
                    Block.makeCuboidShape(3, 6, 0, 13, 12, 8),
                    Block.makeCuboidShape(3, 4, 0, 13, 12, 8)
            };

    public static final VoxelShape[] NORTH = new VoxelShape[]
            {
                    Block.makeCuboidShape(6, 6, 8, 10, 10, 16),
                    Block.makeCuboidShape(6, 4, 8, 10, 10, 16),
                    Block.makeCuboidShape(3, 4, 8, 10, 10, 16),
                    Block.makeCuboidShape(3, 4, 8, 13, 10, 16),
                    Block.makeCuboidShape(3, 4, 8, 13, 12, 16)
            };

    public static final VoxelShape[] EAST = new VoxelShape[]
            {
                    Block.makeCuboidShape(0, 6, 6, 8, 10, 10),
                    Block.makeCuboidShape(0, 4, 6, 8, 10, 10),
                    Block.makeCuboidShape(0, 4, 3, 8, 10, 10),
                    Block.makeCuboidShape(0, 4, 3, 8, 10, 13),
                    Block.makeCuboidShape(0, 4, 3, 8, 12, 13)
            };

    public static final VoxelShape[] WEST = new VoxelShape[]
            {
                    Block.makeCuboidShape(8, 6, 6, 16, 10, 10),
                    Block.makeCuboidShape(8, 4, 6, 16, 10, 10),
                    Block.makeCuboidShape(8, 4, 6, 16, 10, 13),
                    Block.makeCuboidShape(8, 4, 3, 16, 10, 13),
                    Block.makeCuboidShape(8, 4, 3, 16, 12, 13)
            };

    private static final Map<Direction, VoxelShape[]> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES.put(Direction.DOWN, DOWN);
        SHAPES.put(Direction.UP, UP);
        SHAPES.put(Direction.NORTH, NORTH);
        SHAPES.put(Direction.SOUTH, SOUTH);
        SHAPES.put(Direction.WEST, WEST);
        SHAPES.put(Direction.EAST, EAST);
    }

    public static VoxelShape get(BlockState state)
    {
        return SHAPES.get(state.get(QuartzCrystalBlock.FACING))[state.get(QuartzCrystalBlock.AMOUNT) - 1];
    }
}
