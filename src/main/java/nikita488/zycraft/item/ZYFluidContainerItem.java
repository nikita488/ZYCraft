package nikita488.zycraft.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.registries.ForgeRegistries;
import nikita488.zycraft.init.ZYGroups;

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
        if (stack.hasTag() && stack.getTag().contains(FluidHandlerItemStackSimple.FLUID_NBT_KEY, Constants.NBT.TAG_COMPOUND))
            return filledStackSize;

        return super.getItemStackLimit(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        if (world == null)
            return;

        FluidStack fluid = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);

        if (!fluid.isEmpty())
            tooltip.add(((IFormattableTextComponent)fluid.getDisplayName()).mergeStyle(TextFormatting.GRAY));

        if (capacity > FluidAttributes.BUCKET_VOLUME)
            tooltip.add(new TranslationTextComponent("%d/%d mB", fluid.getAmount(), capacity).mergeStyle(TextFormatting.GRAY));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemGroup(group, items);

        if (group != ItemGroup.SEARCH && group != ZYGroups.FLUIDS)
            return;

        for (Fluid fluid : ForgeRegistries.FLUIDS)
        {
            if (!fluid.isSource(fluid.getDefaultState()))
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT tag)
    {
        return new FluidHandlerItemStackSimple(stack, capacity);
    }
}
