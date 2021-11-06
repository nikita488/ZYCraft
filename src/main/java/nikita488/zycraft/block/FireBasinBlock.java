package nikita488.zycraft.block;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class FireBasinBlock extends Block
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public FireBasinBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader reader, BlockPos pos, Direction side)
    {
        return side == Direction.UP;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        if (state.getValue(POWERED))
        {
            if (CampfireBlock.canLight(aboveState))
                aboveState = aboveState.setValue(BlockStateProperties.LIT, true);
            else if (aboveState.isAir())
                aboveState = SoulFireBlock.canSurviveOnBlock(state.getBlock()) ? Blocks.SOUL_FIRE.defaultBlockState() : Blocks.FIRE.defaultBlockState();
            else
                return;

            level.playSound(null, abovePos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(abovePos, aboveState, Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
        else if (aboveState.getBlock() instanceof AbstractFireBlock)
            level.setBlockAndUpdate(abovePos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        if (level.hasNeighborSignal(pos) != state.getValue(POWERED))
            level.setBlock(pos, state.cycle(POWERED), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
    }
}
