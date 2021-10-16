package nikita488.zycraft.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RecipePatternSlot extends Slot
{
    private final AbstractContainerMenu menu;

    public RecipePatternSlot(AbstractContainerMenu menu, Container inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPickup(Player player)
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
