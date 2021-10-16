package nikita488.zycraft.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class SerializableCraftingInventory extends CraftingContainer
{
    public SerializableCraftingInventory(AbstractContainerMenu container, int width, int height)
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

    public void load(CompoundTag tag)
    {
        ContainerHelper.loadAllItems(tag, items);
    }

    public CompoundTag save(CompoundTag tag)
    {
        return ContainerHelper.saveAllItems(tag, items);
    }

    public NonNullList<ItemStack> getItems()
    {
        return items;
    }
}
