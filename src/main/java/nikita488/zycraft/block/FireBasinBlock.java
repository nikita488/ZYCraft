package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;

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
    public boolean isFireSource(BlockState state, LevelReader reader, BlockPos pos, Direction side)
    {
        return side == Direction.UP;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        boolean flammable = false;

        if (state.getValue(POWERED))
        {
            if (CampfireBlock.canLight(aboveState) || CandleBlock.canLight(aboveState) || CandleCakeBlock.canLight(aboveState))
            {
                aboveState = aboveState.setValue(BlockStateProperties.LIT, true);
                level.gameEvent(GameEvent.BLOCK_CHANGE, abovePos);
            }
            else if (aboveState.isAir())
            {
                aboveState = SoulFireBlock.canSurviveOnBlock(state) ? Blocks.SOUL_FIRE.defaultBlockState() : Blocks.FIRE.defaultBlockState();
                level.gameEvent(GameEvent.BLOCK_PLACE, abovePos);
            }
            else if (aboveState.isFlammable(level, abovePos, Direction.DOWN))
                flammable = true;
            else
                return;

            if (flammable)
            {
                aboveState.onCaughtFire(level, abovePos, Direction.DOWN, null);

                if (aboveState.getBlock() instanceof TntBlock)
                    level.removeBlock(abovePos, false);
            }
            else
            {
                level.playSound(null, abovePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(abovePos, aboveState, UPDATE_ALL_IMMEDIATE);
            }
        }
        else if (aboveState.getBlock() instanceof BaseFireBlock)
        {
            level.levelEvent(LevelEvent.SOUND_EXTINGUISH_FIRE, abovePos, -1);
            level.setBlockAndUpdate(abovePos, Blocks.AIR.defaultBlockState());
            level.gameEvent(GameEvent.BLOCK_DESTROY, abovePos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        if (level.hasNeighborSignal(pos) != state.getValue(POWERED))
            level.setBlock(pos, state.cycle(POWERED), UPDATE_CLIENTS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
    }
}
