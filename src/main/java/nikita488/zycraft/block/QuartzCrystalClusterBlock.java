package nikita488.zycraft.block;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.block.shape.ClusterShapes;
import nikita488.zycraft.init.ZYDamageSources;
import nikita488.zycraft.util.ParticleSpawn;

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
    public boolean canBeReplaced(BlockState state, BlockItemUseContext context)
    {
        return context.getItemInHand().getItem() == asItem() && state.getValue(AMOUNT) < 5 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        return state.getBlock() == this ? state.setValue(AMOUNT, Math.min(state.getValue(AMOUNT) + 1, 5)) : defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
    {
        Direction facing = state.getValue(FACING);
        BlockPos oppositePos = pos.relative(facing.getOpposite());
        BlockState oppositeState = world.getBlockState(oppositePos);
        return (facing == Direction.UP && oppositeState.getBlock() == Blocks.HOPPER) || oppositeState.isFaceSturdy(world, oppositePos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
    {
        if (facing.getOpposite() != state.getValue(FACING) || state.canSurvive(world, currentPos))
            return state;

        return Blocks.AIR.defaultBlockState();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
    {
        if (entity instanceof ItemEntity) return;
        entity.hurt(ZYDamageSources.QUARTZ_CRYSTAL_CLUSTER, state.getValue(AMOUNT));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, AMOUNT);
    }
}
