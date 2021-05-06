package nikita488.zycraft.inventory.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RecipePatternSlot extends Slot
{
    private final Container container;

    public RecipePatternSlot(Container container, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.container = container;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player)
    {
        return false;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public void putStack(ItemStack stack)
    {
        super.putStack(stack);
        container.onCraftMatrixChanged(inventory);
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        ItemStack stack = super.decrStackSize(amount);

        if (!stack.isEmpty())
            container.onCraftMatrixChanged(inventory);

        return stack;
    }
}
