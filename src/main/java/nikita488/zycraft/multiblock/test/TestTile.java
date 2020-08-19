package nikita488.zycraft.multiblock.test;

import net.minecraft.tileentity.TileEntityType;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;

public class TestTile extends MultiChildTile
{
    public TestTile(TileEntityType<TestTile> type)
    {
        super(type);
    }

    @Override
    public void onMultiValidation(MultiBlock multiBlock, MultiValidationType type)
    {
        super.onMultiValidation(multiBlock, type);
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock, MultiInvalidationType type)
    {
        super.onMultiInvalidation(multiBlock, type);
    }
}
