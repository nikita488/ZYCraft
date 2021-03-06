package nikita488.zycraft.inventory.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FluidHandlerSlot extends SlotItemHandler
{
    public FluidHandlerSlot(IItemHandler handler, int index, int x, int y)
    {
        super(handler, index, x, y);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }
}
