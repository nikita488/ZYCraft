package nikita488.zycraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;

public class AluminiumFoilItem extends ZYFluidContainerItem
{
    public AluminiumFoilItem(Properties properties)
    {
        super(properties, 16 * FluidAttributes.BUCKET_VOLUME, 1);
    }

    @Override
    protected IFluidHandlerItem createFluidHandler(ItemStack stack)
    {
        return new FluidHandlerItemStack(stack, capacity);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT tag)
    {
        return new FluidHandlerItemStack(stack, capacity);
    }
}
