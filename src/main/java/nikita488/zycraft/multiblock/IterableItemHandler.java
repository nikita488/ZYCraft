package nikita488.zycraft.multiblock;

import com.google.common.collect.AbstractIterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableItemHandler extends ItemStackHandler implements Iterable<ItemStack>
{
    public IterableItemHandler()
    {
        this(1);
    }

    public IterableItemHandler(int size)
    {
        this(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    public IterableItemHandler(NonNullList<ItemStack> stacks)
    {
        this.stacks = stacks;
    }

    @Override
    public Iterator<ItemStack> iterator()
    {
        return new AbstractIterator<ItemStack>()
        {
            int slot = 0;

            @Override
            protected ItemStack computeNext()
            {
                return slot >= getSlots() ? endOfData() : getStackInSlot(slot++);
            }
        };
    }

    public Stream<ItemStack> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }
}
