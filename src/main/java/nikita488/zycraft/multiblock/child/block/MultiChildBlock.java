package nikita488.zycraft.multiblock.child.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    public abstract BlockEntity createTileEntity(BlockState state, BlockGetter getter);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).use(state, level, pos, player, hand, hitResult) : InteractionResult.CONSUME;
    }

    @Override
    public int getLightValue(BlockState state, BlockGetter getter, BlockPos pos)
    {
        BlockEntity blockEntity = getter.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).getLightValue(state, getter, pos) : state.getLightEmission();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof IMultiChild ? ((IMultiChild)blockEntity).getAnalogOutputSignal(state, level, pos) : 0;
    }
}
