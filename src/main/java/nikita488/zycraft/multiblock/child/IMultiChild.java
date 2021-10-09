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

    default ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return parentCount() == 1 ? getParent().onBlockActivated(state, world, pos, player, hand, hit) : ActionResultType.CONSUME;
    }

    default int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        int lightValue = state.getLightValue();

        for (MultiBlock multiBlock : parentMultiBlocks())
            lightValue = Math.max(lightValue, multiBlock.getLightValue(state, world, pos));

        return lightValue;
    }

    default int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return 0;
    }
}
