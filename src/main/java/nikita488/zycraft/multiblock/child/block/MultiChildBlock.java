package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.multiblock.child.IMultiChild;

import javax.annotation.Nullable;

public abstract class MultiChildBlock extends Block
{
    public MultiChildBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IMultiChild ? ((IMultiChild)tile).onBlockActivated(state, world, pos, player, hand, hit) : ActionResultType.CONSUME;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IMultiChild ? ((IMultiChild)tile).getLightValue(state, world, pos) : state.getLightValue();
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof IMultiChild ? ((IMultiChild)tile).getComparatorInputOverride(state, world, pos) : 0;
    }
}
