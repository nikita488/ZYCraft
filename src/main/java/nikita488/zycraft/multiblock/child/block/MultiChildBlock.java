package nikita488.zycraft.multiblock.child.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nikita488.zycraft.multiblock.child.IMultiChild;

public abstract class MultiChildBlock extends Block implements EntityBlock
{
    public MultiChildBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return level.getBlockEntity(pos) instanceof IMultiChild child ? child.use(state, level, pos, player, hand, hitResult) : InteractionResult.CONSUME;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return getter.getBlockEntity(pos) instanceof IMultiChild child ? child.getLightEmission(state, getter, pos) : state.getLightEmission();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return level.getBlockEntity(pos) instanceof IMultiChild child ? child.getAnalogOutputSignal(state, level, pos) : 0;
    }
}
