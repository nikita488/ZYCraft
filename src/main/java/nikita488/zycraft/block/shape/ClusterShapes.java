package nikita488.zycraft.block.shape;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;

import java.util.EnumMap;
import java.util.Map;

public class ClusterShapes
{
    public static final VoxelShape[] DOWN = new VoxelShape[] {
            Block.box(6D, 6D, 6D, 10D, 16D, 10D),
            Block.box(6D, 6D, 6D, 10D, 16D, 14D),
            Block.box(2D, 6D, 6D, 10D, 16D, 14D),
            Block.box(2D, 6D, 6D, 14D, 16D, 14D),
            Block.box(2D, 6D, 2D, 14D, 16D, 14D)
    };

    public static final VoxelShape[] UP = new VoxelShape[] {
            Block.box(6D, 0D, 6D, 10D, 10D, 10D),
            Block.box(6D, 0D, 2D, 10D, 10D, 10D),
            Block.box(2D, 0D, 2D, 10D, 10D, 10D),
            Block.box(2D, 0D, 2D, 14D, 10D, 10D),
            Block.box(2D, 0D, 2D, 14D, 10D, 14D)
    };

    public static final VoxelShape[] NORTH = new VoxelShape[] {
            Block.box(6D, 6D, 6D, 10D, 10D, 16D),
            Block.box(6D, 6D, 6D, 10D, 14D, 16D),
            Block.box(6D, 6D, 6D, 14D, 14D, 16D),
            Block.box(2D, 6D, 6D, 14D, 14D, 16D),
            Block.box(2D, 2D, 6D, 14D, 14D, 16D)
    };

    public static final VoxelShape[] SOUTH = new VoxelShape[] {
            Block.box(6D, 6D, 0D, 10D, 10D, 10D),
            Block.box(6D, 6D, 0D, 10D, 14D, 10D),
            Block.box(2D, 6D, 0D, 10D, 14D, 10D),
            Block.box(2D, 6D, 0D, 14D, 14D, 10D),
            Block.box(2D, 2D, 0D, 14D, 14D, 10D)
    };

    public static final VoxelShape[] WEST = new VoxelShape[] {
            Block.box(6D, 6D, 6D, 16D, 10D, 10D),
            Block.box(6D, 6D, 6D, 16D, 14D, 10D),
            Block.box(6D, 6D, 2D, 16D, 14D, 10D),
            Block.box(6D, 6D, 2D, 16D, 14D, 14D),
            Block.box(6D, 2D, 2D, 16D, 14D, 14D)
    };

    public static final VoxelShape[] EAST = new VoxelShape[] {
            Block.box(0D, 6D, 6D, 10D, 10D, 10D),
            Block.box(0D, 6D, 6D, 10D, 14D, 10D),
            Block.box(0D, 6D, 6D, 10D, 14D, 14D),
            Block.box(0D, 6D, 2D, 10D, 14D, 14D),
            Block.box(0D, 2D, 2D, 10D, 14D, 14D)
    };

    private static final Map<Direction, VoxelShape[]> SHAPES = Util.make(new EnumMap<>(Direction.class), shapes ->
    {
        shapes.put(Direction.DOWN, DOWN);
        shapes.put(Direction.UP, UP);
        shapes.put(Direction.NORTH, NORTH);
        shapes.put(Direction.SOUTH, SOUTH);
        shapes.put(Direction.WEST, WEST);
        shapes.put(Direction.EAST, EAST);
    });

    public static VoxelShape get(BlockState state)
    {
        Direction facing = state.getValue(QuartzCrystalClusterBlock.FACING);
        int amount = state.getValue(QuartzCrystalClusterBlock.AMOUNT);
        return SHAPES.get(facing)[amount - 1];
    }
}
