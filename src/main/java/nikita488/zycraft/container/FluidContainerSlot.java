package nikita488.zycraft.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FluidContainerSlot extends SlotItemHandler
{
    public FluidContainerSlot(IItemHandler handler, int index, int x, int y)
    {
        super(handler, index, x, y);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }
}
