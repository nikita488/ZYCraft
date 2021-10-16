package nikita488.zycraft.multiblock.child.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nikita488.zycraft.init.ZYTiles;

import javax.annotation.Nullable;

public class MultiAirBlock extends MultiChildBlock
{
    public MultiAirBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter getter)
    {
        return ZYTiles.MULTI_AIR.create();
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx)
    {
        return Shapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx)
    {
        return Shapes.empty();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx)
    {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return 1F;
    }

    @Override
    public boolean isAir(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return false;
    }
}