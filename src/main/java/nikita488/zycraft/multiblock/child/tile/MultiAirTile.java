package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import nikita488.zycraft.multiblock.MultiBlock;

public class MultiAirTile extends MultiChildTile
{
    public MultiAirTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!world.isRemote())
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}
