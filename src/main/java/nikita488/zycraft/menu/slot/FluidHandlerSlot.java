package nikita488.zycraft.menu.slot;

import net.minecraft.world.item.ItemStack;
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
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }
}
