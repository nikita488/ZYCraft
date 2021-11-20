package nikita488.zycraft.multiblock.child.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.multiblock.MultiBlock;

public class MultiAirBlockEntity extends MultiChildBlockEntity
{
    public MultiAirBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!level.isClientSide())
            level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
    }
}
