package nikita488.zycraft.multiblock.child.block.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.block.entity.ZYBlockEntity;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.IMultiChild;

public class MultiChildBlockEntity extends ZYBlockEntity implements IMultiChild
{
    public static final int FIRST_AND_ONLY = -1;
    protected final ObjectList<MultiBlock> parentMultiBlocks = new ObjectArrayList<>();

    public MultiChildBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();

        if (!level.isClientSide())
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

            LazyOptional<T> capability = multiBlock.getCapability(type, worldPosition);

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
