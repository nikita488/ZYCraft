package nikita488.zycraft.block;

import nikita488.zycraft.init.ZYDamageSources;
import nikita488.zycraft.util.ParticleSpawn;
import nikita488.zycraft.block.shape.ClusterShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class QuartzCrystalClusterBlock extends Block
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 5);

    public QuartzCrystalClusterBlock(Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState().with(FACING, Direction.DOWN).with(AMOUNT, 1));
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext context)
    {
        return context.getItem().getItem() == asItem() && state.get(AMOUNT) < 5 || super.isReplaceable(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = context.getWorld().getBlockState(context.getPos());
        return state.getBlock() == this ? state.with(AMOUNT, Math.min(state.get(AMOUNT) + 1, 5)) : getDefaultState().with(FACING, context.getFace());
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        Direction facing = state.get(FACING);
        BlockPos oppositePos = pos.offset(facing.getOpposite());
        BlockState oppositeState = world.getBlockState(oppositePos);
        return (facing == Direction.UP && oppositeState.getBlock() == Blocks.HOPPER) || oppositeState.isSolidSide(world, oppositePos, facing);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing.getOpposite() != state.get(FACING) || state.isValidPosition(world, currentPos))
            return state;

        return Blocks.AIR.getDefaultState();
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        ParticleSpawn.quartzCrystalCluster(state, world, pos, rand);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return ClusterShapes.get(state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        if (entity instanceof ItemEntity) return;
        entity.attackEntityFrom(ZYDamageSources.QUARTZ_CRYSTAL_CLUSTER, state.get(AMOUNT));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, AMOUNT);
    }
}
