package nikita488.zycraft.multiblock.tile;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.tile.ZYTile;

public class MultiChildTile extends ZYTile
{
    protected final ObjectList<MultiBlock> parentMultiBlocks = new ObjectArrayList<>();

    public MultiChildTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void remove()
    {
        super.remove();

        if (!world.isRemote)
            for (MultiBlock multiBlock : parentMultiBlocks)//TODO: Check if we need to copy list to avoid CME
                multiBlock.destroy();
    }

    public void onMultiValidation(MultiBlock multiBlock)
    {
        if (!parentMultiBlocks.contains(multiBlock))
            parentMultiBlocks.add(multiBlock);
    }

    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        parentMultiBlocks.remove(multiBlock);
    }

    public <T> LazyOptional<T> getMultiCapability(Capability<T> type, int index)
    {
        LazyOptional<T> foundCapability = LazyOptional.empty();
        int count = 0;

        for (MultiBlock multiBlock : parentMultiBlocks)
        {
            LazyOptional<T> capability = multiBlock.getCapability(type, this);

            if (!capability.isPresent())
                continue;

            if (index < 0)
            {
                if (foundCapability.isPresent())
                    return LazyOptional.empty();

                foundCapability = capability;
            }
            else
            {
                if (index == count)
                    return capability;

                count++;
            }
        }

        return index < 0 ? foundCapability : LazyOptional.empty();
    }

    public boolean hasParent()
    {
        return !parentMultiBlocks.isEmpty();
    }

    public ObjectList<MultiBlock> parentMultiBlocks()
    {
        return parentMultiBlocks;
    }
}
