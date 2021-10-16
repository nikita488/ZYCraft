package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : ZYConstants.DIRECTIONS)
            transform(level, pos, pos.relative(side));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        transform(level, pos, relativePos);
    }

    private void transform(Level level, BlockPos pos, BlockPos relativePos)
    {
        FluidState fluidState = level.getFluidState(relativePos);

        if (fluidState.is(FluidTags.WATER))
        {
            if (level.getBlockState(relativePos).getBlock() instanceof LiquidBlock)
                level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.ICE.defaultBlockState()));
        }
        else if (fluidState.is(FluidTags.LAVA) && level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL))
        {
            level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, Blocks.BASALT.defaultBlockState()));
            level.levelEvent(Constants.WorldEvents.LAVA_EXTINGUISH, relativePos, -1);
        }
    }
}
