package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.FluidUtils;

public class BasicMachineBlock extends Block
{
    private final static Direction[] VALUES = Direction.values();
    private final ZYType type;

    public BasicMachineBlock(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (type == ZYType.GREEN || type == ZYType.RED)
            return;

        for (Direction side : VALUES)
            modifyAdjacentState(world, pos, pos.offset(side));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjacentPos, boolean isMoving)
    {
        if (type == ZYType.GREEN)
        {

        }
        else if (type == ZYType.RED)
        {

        }
        else if (!world.isBlockPowered(pos))
        {
            modifyAdjacentState(world, pos, adjacentPos);
        }
    }

    private void modifyAdjacentState(World world, BlockPos pos, BlockPos adjacentPos)
    {
        BlockState blockState = world.getBlockState(adjacentPos);
        FluidState fluidState = world.getFluidState(adjacentPos);

        if (fluidState.isEmpty())
            return;

        if (type == ZYType.BLUE)
            FluidUtils.turnLavaIntoBlock(world, adjacentPos, fluidState, (fluidState.isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE).getDefaultState());
        else if (type == ZYType.DARK)
            FluidUtils.voidFluid(blockState, world, adjacentPos);
        else if (type == ZYType.LIGHT && (!world.getBlockState(pos.down()).matchesBlock(Blocks.SOUL_SOIL) || !FluidUtils.turnLavaIntoBlock(world, adjacentPos, fluidState, Blocks.BASALT.getDefaultState())))
            FluidUtils.turnWaterIntoIce(blockState, world, adjacentPos, fluidState);
    }
}
