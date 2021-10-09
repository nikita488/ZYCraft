package nikita488.zycraft.menu.slot;

import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ZYSlot extends SlotItemHandler
{
    public ZYSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isSameInventory(Slot other)
    {
        return other instanceof SlotItemHandler && getItemHandler() == ((SlotItemHandler)other).getItemHandler();
    }
}
