package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;

import javax.annotation.Nullable;
import java.util.Random;

public class ZychoriumSoilBlock extends Block
{
    public final static BooleanProperty FLIPPED = ZYBlockStateProperties.ZYCHORIUM_SOIL_FLIPPED;

    public ZychoriumSoilBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FLIPPED, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random)
    {
        Direction dir = state.getValue(FLIPPED) ? Direction.DOWN : Direction.UP;
        BlockPos tickPos = pos.relative(dir);
        BlockState stateToTick = world.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.isRandomlyTicking())
            return;

        if (blockToTick instanceof IPlantable || (blockToTick instanceof BonemealableBlock && ((BonemealableBlock)blockToTick).isValidBonemealTarget(world, tickPos, stateToTick, false)))
        {
            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos().set(tickPos);

            while (blockToTick == stateToTick.getBlock() && stateToTick.isRandomlyTicking())
                stateToTick = world.getBlockState(checkPos.move(dir));

            stateToTick = world.getBlockState(checkPos.move(dir.getOpposite()));
            stateToTick.randomTick(world, checkPos.immutable(), random);
        }
        else if (blockToTick == this)
        {
            stateToTick.randomTick(world, tickPos, random);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(FLIPPED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter getter, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return facing == (state.getValue(FLIPPED) ? Direction.DOWN : Direction.UP);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        if (state.getValue(FLIPPED) != level.hasNeighborSignal(pos))
            level.setBlock(pos, state.cycle(FLIPPED), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FLIPPED);
    }
}
