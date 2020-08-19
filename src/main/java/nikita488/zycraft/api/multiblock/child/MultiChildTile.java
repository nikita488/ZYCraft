package nikita488.zycraft.api.multiblock.child;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tileentity.TileEntityType;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.tile.BaseTile;

public class MultiChildTile extends BaseTile
{
    protected final ObjectArrayList<MultiBlock> parentMultiBlocks = new ObjectArrayList<>();

    public MultiChildTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void remove()
    {
        super.remove();

        if (!world.isRemote)
            for (MultiBlock multiBlock : parentMultiBlocks)
                multiBlock.world().removeMultiBlock(multiBlock, MultiInvalidationType.DESTROY);
    }

    public void onMultiValidation(MultiBlock multiBlock, MultiValidationType type)
    {
        parentMultiBlocks.add(multiBlock);
    }

    public void onMultiInvalidation(MultiBlock multiBlock, MultiInvalidationType type)
    {
        parentMultiBlocks.remove(multiBlock);
    }

    public boolean hasParent()
    {
        return !parentMultiBlocks.isEmpty();
    }

    public ObjectArrayList<MultiBlock> parentMultiBlocks()
    {
        return parentMultiBlocks;
    }
}
