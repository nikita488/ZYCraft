package nikita488.zycraft.multiblock.child;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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

    default ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        return parentCount() == 1 ? getParent().onBlockActivated(state, level, pos, player, hand, hitResult) : ActionResultType.CONSUME;
    }

    default int getLightValue(BlockState state, IBlockReader getter, BlockPos pos)
    {
        int emission = state.getLightEmission();

        for (MultiBlock multiBlock : parentMultiBlocks())
            emission = Math.max(emission, multiBlock.getLightValue(state, getter, pos));

        return emission;
    }

    default int getAnalogOutputSignal(BlockState state, World level, BlockPos pos)
    {
        return 0;
    }
}
