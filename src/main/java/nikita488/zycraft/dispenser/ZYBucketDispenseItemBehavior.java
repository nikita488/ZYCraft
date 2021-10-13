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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.util.FluidUtils;

import java.util.Optional;

public class ZYBucketDispenseItemBehavior extends DefaultDispenseItemBehavior
{
    public static final ZYBucketDispenseItemBehavior INSTANCE = new ZYBucketDispenseItemBehavior();
    private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

    @Override
    public ItemStack execute(IBlockSource source, ItemStack stack)
    {
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(stack);

        if (!capability.isPresent())
            return stack;

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        World world = source.getLevel();
        BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

        if (!containedFluid.isEmpty())
        {
            if (FluidUtils.tryPlaceFluid(containedFluid, null, world, pos, null))
                return new ItemStack(stack.getItem());

            return defaultBehaviour.dispense(source, stack);
        }

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IBucketPickupHandler)
        {
            Fluid fluid = ((IBucketPickupHandler)block).takeLiquid(world, pos, state);

            if (!(fluid instanceof FlowingFluid))
                return super.execute(source, stack);

            FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

            if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
                return super.execute(source, stack);

            handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            stack.shrink(1);

            ItemStack filledContainer = handler.getContainer();

            if (stack.isEmpty())
                return filledContainer;

            if (source.<DispenserTileEntity>getEntity().addItem(filledContainer) < 0)
                defaultBehaviour.dispense(source, filledContainer);

            return stack;
        }

        return super.execute(source, stack);
    }
}
