package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import nikita488.zycraft.util.ZYConstants;

public class ZychoriumIceBlock extends Block
{
    public ZychoriumIceBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        transform(level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        transform(level, pos);
    }

    private void transform(World level, BlockPos pos)
    {
        if (level.hasNeighborSignal(pos))
            return;

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            BlockPos relativePos = pos.relative(side);
            FluidState fluidState = level.getFluidState(relativePos);

            if (fluidState.is(FluidTags.WATER))
            {
                if (level.getBlockState(relativePos).getBlock() instanceof FlowingFluidBlock)
                    level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.ICE.defaultBlockState()));
            }
            else if (fluidState.is(FluidTags.LAVA) && level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL))
            {
                level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.BASALT.defaultBlockState()));
                level.levelEvent(Constants.WorldEvents.LAVA_EXTINGUISH, relativePos, -1);
            }
        }
    }
}
