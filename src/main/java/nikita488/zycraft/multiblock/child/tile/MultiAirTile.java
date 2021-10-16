package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import nikita488.zycraft.multiblock.MultiBlock;

public class MultiAirTile extends MultiChildTile
{
    public MultiAirTile(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!level.isClientSide())
            level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
    }
}
