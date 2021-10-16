package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : ZYConstants.DIRECTIONS)
            voidFluid(level, pos.relative(side));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        voidFluid(level, relativePos);
    }

    private void voidFluid(Level level, BlockPos pos)
    {
        FluidUtils.voidFluid(level, pos, fluidState -> !fluidState.isEmpty());
    }

    @Override
    public int getDrainAmount(BlockState state, Level level, BlockPos pos, @Nullable Direction side)
    {
        return 50;
    }
}
