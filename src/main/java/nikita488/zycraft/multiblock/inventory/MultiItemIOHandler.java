package nikita488.zycraft.multiblock.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import nikita488.zycraft.block.state.properties.ItemIOMode;

import java.util.EnumMap;

public class MultiItemIOHandler extends ItemStackHandler implements IMultiItemIOHandler
{
    private final EnumMap<ItemIOMode, IItemHandlerModifiable> itemHandlers = new EnumMap<>(ItemIOMode.class);
    private final int[] activeModes = new int[ItemIOMode.VALUES.length];
    private int inputCount, outputCount;

    public MultiItemIOHandler()
    {
        super();
        itemHandlers.put(ItemIOMode.ANY, this);
    }

    public MultiItemIOHandler(int size)
    {
        super(size);
        itemHandlers.put(ItemIOMode.ANY, this);
    }

    public MultiItemIOHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
        itemHandlers.put(ItemIOMode.ANY, this);
    }

    @Override
    public boolean isSupported(ItemIOMode mode)
    {
        return itemHandlers.containsKey(mode);
    }

    @Override
    public IItemHandler access(ItemIOMode mode)
    {
        IItemHandlerModifiable handler = itemHandlers.get(mode);
        return handler != null ? handler : EmptyHandler.INSTANCE;
    }

    @Override
    public void setActive(ItemIOMode mode)
    {
        this.activeModes[mode.ordinal()]++;
    }

    @Override
    public void setInactive(ItemIOMode mode)
    {
        if (isActive(mode))
            this.activeModes[mode.ordinal()]--;
    }

    @Override
    public boolean isActive(ItemIOMode mode)
    {
        return activeModes[mode.ordinal()] > 0;
    }

    public MultiItemIOHandler input(int slot)
    {
        return input(slot, slot + 1);
    }

    public MultiItemIOHandler input(int minSlot, int maxSlotExclusive)
    {
        if (inputCount > 1)
        {
            return this;
        }
        else if (inputCount > 0)
        {
            itemHandlers.put(ItemIOMode.IN1, itemHandlers.get(ItemIOMode.ALL_IN));
            itemHandlers.put(ItemIOMode.IN2, new RangedWrapper(this, minSlot, maxSlotExclusive));
            itemHandlers.put(ItemIOMode.ALL_IN, new CombinedInvWrapper(itemHandlers.get(ItemIOMode.IN1), itemHandlers.get(ItemIOMode.IN2)));
        }
        else
        {
            itemHandlers.put(ItemIOMode.ALL_IN, new RangedWrapper(this, minSlot, maxSlotExclusive));
        }

        this.inputCount++;
        return this;
    }

    public MultiItemIOHandler output(int slot)
    {
        return output(slot, slot + 1);
    }

    public MultiItemIOHandler output(int minSlot, int maxSlotExclusive)
    {
        if (outputCount > 1)
        {
            return this;
        }
        else if (outputCount > 0)
        {
            itemHandlers.put(ItemIOMode.OUT1, itemHandlers.get(ItemIOMode.ALL_OUT));
            itemHandlers.put(ItemIOMode.OUT2, new RangedWrapper(this, minSlot, maxSlotExclusive));
            itemHandlers.put(ItemIOMode.ALL_OUT, new CombinedInvWrapper(itemHandlers.get(ItemIOMode.OUT1), itemHandlers.get(ItemIOMode.OUT2)));
        }
        else
        {
            itemHandlers.put(ItemIOMode.ALL_OUT, new RangedWrapper(this, minSlot, maxSlotExclusive));
        }

        this.outputCount++;
        return this;
    }
}
