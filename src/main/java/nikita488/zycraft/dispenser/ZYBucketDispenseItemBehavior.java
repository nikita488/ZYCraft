package nikita488.zycraft.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.util.FluidUtils;

import java.util.Optional;

public class ZYBucketDispenseItemBehavior extends DefaultDispenseItemBehavior
{
    public static final ZYBucketDispenseItemBehavior INSTANCE = new ZYBucketDispenseItemBehavior();
    private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

    @Override
    public ItemStack execute(BlockSource source, ItemStack stack)
    {
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(stack);

        if (!capability.isPresent())
            return stack;

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        Level level = source.getLevel();
        BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

        if (!containedFluid.isEmpty())
            return FluidUtils.tryPlaceFluid(containedFluid, null, level, pos, null) ? new ItemStack(stack.getItem()) : defaultBehaviour.dispense(source, stack);

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!(block instanceof BucketPickup pickup))
            return super.execute(source, stack);

        ItemStack bucketStack = pickup.pickupBlock(level, pos, state);

        if (bucketStack.isEmpty())
            return super.execute(source, stack);

        level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);

        FluidStack fluidStack = FluidUtil.getFluidContained(bucketStack).orElse(FluidStack.EMPTY);

        if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
            return super.execute(source, stack);

        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        stack.shrink(1);

        ItemStack filledContainer = handler.getContainer();

        if (stack.isEmpty())
            return filledContainer;

        if (source.<DispenserBlockEntity>getEntity().addItem(filledContainer) < 0)
            defaultBehaviour.dispense(source, filledContainer);

        return stack;

    }
}
