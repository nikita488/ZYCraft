package nikita488.zycraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.init.ZYCreativeModeTabs;
import nikita488.zycraft.init.ZYLang;

import javax.annotation.Nullable;
import java.util.List;

public class ZYFluidContainerItem extends Item
{
    protected final int capacity, filledStackSize;

    public ZYFluidContainerItem(Properties properties, int capacity, int filledStackSize)
    {
        super(properties);
        this.capacity = capacity;
        this.filledStackSize = filledStackSize;
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if (stack.hasTag() && stack.getTag().contains(FluidHandlerItemStackSimple.FLUID_NBT_KEY, Tag.TAG_COMPOUND))
            return filledStackSize;

        return super.getItemStackLimit(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        if (level == null)
            return;

        FluidStack fluid = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);

        if (!fluid.isEmpty())
            tooltip.add(((MutableComponent)fluid.getDisplayName()).withStyle(ChatFormatting.GRAY));

        if (capacity > FluidType.BUCKET_VOLUME)
            tooltip.add(ZYLang.copy(ZYLang.FLUID_TANK_FILLED, fluid.getAmount(), capacity));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items)
    {
        super.fillItemCategory(group, items);

        if (group != CreativeModeTab.TAB_SEARCH && group != ZYCreativeModeTabs.FLUIDS)
            return;

        for (Fluid fluid : ForgeRegistries.FLUIDS)
        {
            if (!fluid.isSource(fluid.defaultFluidState()))
                continue;

            ItemStack stack = new ItemStack(this);
            FluidStack fluidStack = new FluidStack(fluid, capacity);
            IFluidHandlerItem handler = createFluidHandler(stack);

            if (handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE) == fluidStack.getAmount())
                items.add(handler.getContainer());
        }
    }

    protected IFluidHandlerItem createFluidHandler(ItemStack stack)
    {
        return new FluidHandlerItemStackSimple(stack, capacity);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag tag)
    {
        return new FluidHandlerItemStackSimple(stack, capacity);
    }
}
