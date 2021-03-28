package nikita488.zycraft.block.shape;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;

public class ClusterShapes
{
    public static final VoxelShape[] DOWN = new VoxelShape[] {
            Block.makeCuboidShape(6, 6, 6, 10, 16, 10),
            Block.makeCuboidShape(6, 6, 6, 10, 16, 14),
            Block.makeCuboidShape(2, 6, 6, 10, 16, 14),
            Block.makeCuboidShape(2, 6, 6, 14, 16, 14),
            Block.makeCuboidShape(2, 6, 2, 14, 16, 14)
    };

    public static final VoxelShape[] UP = new VoxelShape[] {
            Block.makeCuboidShape(6, 0, 6, 10, 10, 10),
            Block.makeCuboidShape(6, 0, 2, 10, 10, 10),
            Block.makeCuboidShape(2, 0, 2, 10, 10, 10),
            Block.makeCuboidShape(2, 0, 2, 14, 10, 10),
            Block.makeCuboidShape(2, 0, 2, 14, 10, 14)
    };

    public static final VoxelShape[] NORTH = new VoxelShape[] {
            Block.makeCuboidShape(6, 6, 6, 10, 10, 16),
            Block.makeCuboidShape(6, 6, 6, 10, 14, 16),
            Block.makeCuboidShape(6, 6, 6, 14, 14, 16),
            Block.makeCuboidShape(2, 6, 6, 14, 14, 16),
            Block.makeCuboidShape(2, 2, 6, 14, 14, 16)
    };

    public static final VoxelShape[] SOUTH = new VoxelShape[] {
            Block.makeCuboidShape(6, 6, 0, 10, 10, 10),
            Block.makeCuboidShape(6, 6, 0, 10, 14, 10),
            Block.makeCuboidShape(2, 6, 0, 10, 14, 10),
            Block.makeCuboidShape(2, 6, 0, 14, 14, 10),
            Block.makeCuboidShape(2, 2, 0, 14, 14, 10)
    };

    public static final VoxelShape[] WEST = new VoxelShape[] {
            Block.makeCuboidShape(6, 6, 6, 16, 10, 10),
            Block.makeCuboidShape(6, 6, 6, 16, 14, 10),
            Block.makeCuboidShape(6, 6, 2, 16, 14, 10),
            Block.makeCuboidShape(6, 6, 2, 16, 14, 14),
            Block.makeCuboidShape(6, 2, 2, 16, 14, 14)
    };

    public static final VoxelShape[] EAST = new VoxelShape[] {
            Block.makeCuboidShape(0, 6, 6, 10, 10, 10),
            Block.makeCuboidShape(0, 6, 6, 10, 14, 10),
            Block.makeCuboidShape(0, 6, 6, 10, 14, 14),
            Block.makeCuboidShape(0, 6, 2, 10, 14, 14),
            Block.makeCuboidShape(0, 2, 2, 10, 14, 14)
    };

    public static VoxelShape get(BlockState state)
    {
        VoxelShape[] shapes;

        switch (state.get(QuartzCrystalClusterBlock.FACING))
        {
            case DOWN:
                shapes = DOWN;
                break;
            case UP:
                shapes = UP;
                break;
            case NORTH:
                shapes = NORTH;
                break;
            case SOUTH:
                shapes = SOUTH;
                break;
            case WEST:
                shapes = WEST;
                break;
            case EAST:
                shapes = EAST;
                break;
            default:
                shapes = new VoxelShape[0];
        }

        return shapes[state.get(QuartzCrystalClusterBlock.AMOUNT) - 1];
    }
}
