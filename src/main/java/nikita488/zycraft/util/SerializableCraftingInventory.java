package nikita488.zycraft.util;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class SerializableCraftingInventory extends CraftingInventory
{
    public SerializableCraftingInventory(Container eventHandler, int width, int height)
    {
        super(eventHandler, width, height);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        ItemStack stack = super.removeStackFromSlot(slot);

        if (!stack.isEmpty())
            markDirty();

        return stack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = super.decrStackSize(slot, amount);

        if (!stack.isEmpty())
            markDirty();

        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        super.setInventorySlotContents(slot, stack);
        markDirty();
    }

    @Override
    public void clear()
    {
        super.clear();
        markDirty();
    }

    public void load(CompoundNBT tag)
    {
        ItemStackHelper.loadAllItems(tag, stackList);
    }

    public CompoundNBT save(CompoundNBT tag)
    {
        return ItemStackHelper.saveAllItems(tag, stackList);
    }

    public NonNullList<ItemStack> getItems()
    {
        return stackList;
    }
}
