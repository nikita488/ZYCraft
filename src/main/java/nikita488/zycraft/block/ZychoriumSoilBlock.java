package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
    public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random)
    {
        boolean flipped = state.getValue(FLIPPED);
        Direction dir = flipped ? Direction.DOWN : Direction.UP;
        BlockPos tickPos = pos.relative(dir);
        BlockState stateToTick = level.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.isRandomlyTicking())
            return;

        if (blockToTick instanceof IPlantable || (blockToTick instanceof IGrowable && ((IGrowable)blockToTick).isValidBonemealTarget(level, tickPos, stateToTick, false)))
        {
            BlockPos.Mutable checkPos = new BlockPos.Mutable().set(tickPos);

            while (blockToTick == stateToTick.getBlock() && stateToTick.isRandomlyTicking())
                stateToTick = level.getBlockState(checkPos.move(dir));

            stateToTick = level.getBlockState(checkPos.move(dir.getOpposite()));
            stateToTick.randomTick(level, checkPos.immutable(), random);
        }
        else if (blockToTick == this && flipped == stateToTick.getValue(FLIPPED))
        {
            stateToTick.randomTick(level, tickPos, random);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState().setValue(FLIPPED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader getter, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return facing == (state.getValue(FLIPPED) ? Direction.DOWN : Direction.UP);
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        if (state.getValue(FLIPPED) != level.hasNeighborSignal(pos))
            level.setBlock(pos, state.cycle(FLIPPED), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FLIPPED);
    }
}
