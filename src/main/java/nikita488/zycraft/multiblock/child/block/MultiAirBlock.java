package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
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
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.MULTI_AIR.create();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader getter, BlockPos pos, ISelectionContext ctx)
    {
        return VoxelShapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader getter, BlockPos pos, ISelectionContext ctx)
    {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext ctx)
    {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return 1F;
    }

    @Override
    public boolean isAir(BlockState state, IBlockReader getter, BlockPos pos)
    {
        return false;
    }
}