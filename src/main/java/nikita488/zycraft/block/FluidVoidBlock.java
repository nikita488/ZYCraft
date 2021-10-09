package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;

public class FluidVoidBlock extends Block implements IFluidVoid
{
    public FluidVoidBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : ZYConstants.DIRECTIONS)
            voidFluid(world, pos.offset(side));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjPos, boolean isMoving)
    {
        voidFluid(world, adjPos);
    }

    private void voidFluid(World world, BlockPos pos)
    {
        FluidUtils.voidFluid(world, pos, fluidState -> !fluidState.isEmpty());
    }

    @Override
    public int getDrainAmount(BlockState state, World world, BlockPos pos, @Nullable Direction side)
    {
        return 50;
    }
}
