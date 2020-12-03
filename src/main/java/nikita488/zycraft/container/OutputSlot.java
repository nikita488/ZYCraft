package nikita488.zycraft.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class OutputSlot extends SlotItemHandler
{
    public OutputSlot(IItemHandler handler, int index, int x, int y)
    {
        super(handler, index, x, y);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return true;
    }
}
