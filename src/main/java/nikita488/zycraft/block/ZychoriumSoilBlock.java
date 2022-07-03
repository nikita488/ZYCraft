package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;

public class ZychoriumSoilBlock extends Block
{
    public final static BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ZychoriumSoilBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        boolean powered = state.getValue(POWERED);
        Direction dir = powered ? Direction.DOWN : Direction.UP;
        BlockPos tickPos = pos.relative(dir);
        BlockState stateToTick = level.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.isRandomlyTicking())
            return;

        if (blockToTick instanceof IPlantable || (blockToTick instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(level, tickPos, stateToTick, false)))
        {
            BlockPos.MutableBlockPos checkPos = tickPos.mutable();

            while (blockToTick == stateToTick.getBlock() && stateToTick.isRandomlyTicking())
                stateToTick = level.getBlockState(checkPos.move(dir));

            stateToTick = level.getBlockState(checkPos.move(dir.getOpposite()));
            stateToTick.randomTick(level, checkPos.immutable(), random);
        }
        else if (blockToTick == this && powered == stateToTick.getValue(POWERED))
        {
            stateToTick.randomTick(level, tickPos, random);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter getter, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return facing == (state.getValue(POWERED) ? Direction.DOWN : Direction.UP);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return true;
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
