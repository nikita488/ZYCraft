package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class FluidVoidBlock extends Block implements IFluidVoid
{
    private static final Predicate<FluidState> ANY_FLUID = fluidState -> !fluidState.isEmpty();

    public FluidVoidBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        voidFluid(level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        voidFluid(level, pos);
    }

    private void voidFluid(World level, BlockPos pos)
    {
        if (!level.hasNeighborSignal(pos))
            for (Direction side : ZYConstants.DIRECTIONS)
                FluidUtils.voidFluid(level, pos.relative(side), ANY_FLUID);
    }

    @Override
    public int getDrainAmount(BlockState state, World level, BlockPos pos, @Nullable Direction side)
    {
        return 50;
    }
}
