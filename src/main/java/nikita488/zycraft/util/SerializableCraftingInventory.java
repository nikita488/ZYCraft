package nikita488.zycraft.util;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public class SerializableCraftingInventory extends CraftingInventory
{
    public SerializableCraftingInventory(Container container, int width, int height)
    {
        super(container, width, height);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        ItemStack stack = super.removeItem(slot, amount);

        if (!stack.isEmpty())
            setChanged();

        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        super.setItem(slot, stack);
        setChanged();
    }

    @Override
    public void clearContent()
    {
        super.clearContent();
        setChanged();
    }

    public void load(CompoundNBT tag)
    {
        ItemStackHelper.loadAllItems(tag, items);
    }

    public CompoundNBT save(CompoundNBT tag)
    {
        return ItemStackHelper.saveAllItems(tag, items);
    }

    public NonNullList<ItemStack> getItems()
    {
        return items;
    }
}
