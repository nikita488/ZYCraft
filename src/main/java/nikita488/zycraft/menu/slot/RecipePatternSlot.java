package nikita488.zycraft.menu.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RecipePatternSlot extends Slot
{
    private final Container menu;

    public RecipePatternSlot(Container menu, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPickup(PlayerEntity player)
    {
        return false;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public void set(ItemStack stack)
    {
        super.set(stack);
        menu.slotsChanged(container);
    }

    @Override
    public ItemStack remove(int amount)
    {
        ItemStack stack = super.remove(amount);

        if (!stack.isEmpty())
            menu.slotsChanged(container);

        return stack;
    }
}
