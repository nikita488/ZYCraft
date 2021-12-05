package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nikita488.zycraft.init.ZYDamageSources;
import nikita488.zycraft.util.ParticleUtils;

import java.util.Random;

public class QuartzCrystalClusterBlock extends Block
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 5);

    public QuartzCrystalClusterBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN).setValue(AMOUNT, 1));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context)
    {
        return context.getItemInHand().getItem() == asItem() && state.getValue(AMOUNT) < 5 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return state.is(this) ? state.setValue(AMOUNT, Math.min(state.getValue(AMOUNT) + 1, 5)) : defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos)
    {
        Direction facing = state.getValue(FACING);
        BlockPos oppositePos = pos.relative(facing.getOpposite());
        BlockState oppositeState = reader.getBlockState(oppositePos);
        return (facing == Direction.UP && oppositeState.is(Blocks.HOPPER)) || oppositeState.isFaceSturdy(reader, oppositePos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing.getOpposite() != state.getValue(FACING) || state.canSurvive(accessor, currentPos))
            return state;

        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
    {
        ParticleUtils.quartzCrystalCluster(state, level, pos, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context)
    {
        return QuartzCrystalClusterShape.get(state);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if (!(entity instanceof ItemEntity))
            entity.hurt(ZYDamageSources.QUARTZ_CRYSTAL_CLUSTER, state.getValue(AMOUNT));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, AMOUNT);
    }
}
