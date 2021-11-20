package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.ForgeEventFactory;
import nikita488.zycraft.util.ZYConstants;

public class ZychoriumIceBlock extends Block
{
    public ZychoriumIceBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        transform(level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        transform(level, pos);
    }

    private void transform(Level level, BlockPos pos)
    {
        if (level.hasNeighborSignal(pos))
            return;

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            BlockPos relativePos = pos.relative(side);
            FluidState fluidState = level.getFluidState(relativePos);

            if (fluidState.is(FluidTags.WATER))
            {
                if (level.getBlockState(relativePos).getBlock() instanceof LiquidBlock)
                    level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.ICE.defaultBlockState()));
            }
            else if (fluidState.is(FluidTags.LAVA) && level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL))
            {
                level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.BASALT.defaultBlockState()));
                level.levelEvent(LevelEvent.LAVA_FIZZ, relativePos, -1);
            }
        }
    }
}
