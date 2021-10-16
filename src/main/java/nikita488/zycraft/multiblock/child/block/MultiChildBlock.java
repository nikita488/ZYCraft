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
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader getter);

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        TileEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).use(state, level, pos, player, hand, hitResult) : ActionResultType.CONSUME;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader getter, BlockPos pos)
    {
        TileEntity blockEntity = getter.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).getLightValue(state, getter, pos) : state.getLightEmission();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos)
    {
        TileEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).getAnalogOutputSignal(state, level, pos) : 0;
    }
}
