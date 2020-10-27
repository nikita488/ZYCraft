package nikita488.zycraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.item.ZYBucketItem;

import java.util.Optional;

public class ZYBucketDispenseItemBehavior extends DefaultDispenseItemBehavior
{
    public static final ZYBucketDispenseItemBehavior INSTANCE = new ZYBucketDispenseItemBehavior();
    private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        Optional<IFluidHandlerItem> capability = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(stack, 1)).resolve();

        if (!capability.isPresent())
            return stack;

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        World world = source.getWorld();
        BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));

        if (!containedFluid.isEmpty())
        {
            if (ZYBucketItem.tryPlaceFluid(containedFluid.getFluid(), null, world, pos, null))
                return ZYItems.QUARTZ_BUCKET.asStack();

            return defaultBehaviour.dispense(source, stack);
        }

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof IBucketPickupHandler))
            return super.dispenseStack(source, stack);

        Fluid fluid = ((IBucketPickupHandler)block).pickupFluid(world, pos, state);

        if (!(fluid instanceof FlowingFluid))
            return super.dispenseStack(source, stack);

        FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
            return super.dispenseStack(source, stack);

        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        stack.shrink(1);

        ItemStack filledContainer = handler.getContainer();

        if (stack.isEmpty())
            return filledContainer;

        if (source.<DispenserTileEntity>getBlockTileEntity().addItemStack(filledContainer) < 0)
            defaultBehaviour.dispense(source, filledContainer);

        return stack;
    }
}
