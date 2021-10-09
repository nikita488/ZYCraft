package nikita488.zycraft.multiblock.child.tile;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.IMultiChild;
import nikita488.zycraft.tile.ZYTile;

public class MultiChildTile extends ZYTile implements IMultiChild
{
    public static final int FIRST_AND_ONLY = -1;
    protected final ObjectList<MultiBlock> parentMultiBlocks = new ObjectArrayList<>();

    public MultiChildTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void remove()
    {
        super.remove();

        if (!world.isRemote())
            for (MultiBlock multiBlock : parentMultiBlocks)
                multiBlock.destroy();
    }

    public <T> LazyOptional<T> getMultiCapability(Capability<T> type)
    {
        return getMultiCapability(type, FIRST_AND_ONLY);
    }

    public <T> LazyOptional<T> getMultiCapability(Capability<T> type, int index)
    {
        LazyOptional<T> foundCapability = LazyOptional.empty();
        int foundCount = 0;

        for (MultiBlock multiBlock : parentMultiBlocks)
        {
            if (!multiBlock.isValid())
                continue;

            LazyOptional<T> capability = multiBlock.getCapability(type, pos);

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
                if (index == foundCount)
                    return capability;
                foundCount++;
            }
        }

        return index < 0 ? foundCapability : LazyOptional.empty();
    }

    @Override
    public ObjectList<MultiBlock> parentMultiBlocks()
    {
        return parentMultiBlocks;
    }
}
