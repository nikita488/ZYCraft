package nikita488.zycraft.multiblock.child;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nikita488.zycraft.multiblock.MultiBlock;

public interface IMultiChild
{
    default void onMultiValidation(MultiBlock multiBlock)
    {
        if (!hasParent(multiBlock))
            addParent(multiBlock);
    }

    default void onMultiInvalidation(MultiBlock multiBlock)
    {
        removeParent(multiBlock);
    }

    ObjectList<MultiBlock> parentMultiBlocks();

    default int parentCount()
    {
        return parentMultiBlocks().size();
    }

    default boolean hasParents()
    {
        return parentCount() > 0;
    }

    default boolean hasParent(MultiBlock multiBlock)
    {
        return parentMultiBlocks().contains(multiBlock);
    }

    default boolean addParent(MultiBlock multiBlock)
    {
        return parentMultiBlocks().add(multiBlock);
    }

    default boolean removeParent(MultiBlock multiBlock)
    {
        return parentMultiBlocks().remove(multiBlock);
    }

    default MultiBlock getParent()
    {
        return getParent(0);
    }

    default MultiBlock getParent(int index)
    {
        return parentMultiBlocks().get(index);
    }

    default InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return parentCount() == 1 ? getParent().onBlockActivated(state, level, pos, player, hand, hitResult) : InteractionResult.CONSUME;
    }

    default int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos)
    {
        int emission = state.getLightEmission();

        for (MultiBlock multiBlock : parentMultiBlocks())
            emission = Math.max(emission, multiBlock.getLightValue(state, getter, pos));

        return emission;
    }

    default int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return 0;
    }
}
