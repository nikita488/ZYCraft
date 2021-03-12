package nikita488.zycraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.util.FluidUtils;

import java.util.Optional;

import net.minecraft.item.Item.Properties;

public class ZYBucketItem extends ZYFluidContainerItem
{
    public ZYBucketItem(Properties properties)
    {
        this(properties, FluidAttributes.BUCKET_VOLUME);
    }

    public ZYBucketItem(Properties properties, int capacity)
    {
        super(properties, capacity, 1);
    }

    protected ItemStack emptyBucket(IFluidHandlerItem handler, ItemStack stack, PlayerEntity player)
    {
        return !player.abilities.instabuild ? new ItemStack(this) : stack;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        ItemStack heldStack = player.getItemInHand(hand);
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return ActionResult.pass(heldStack);

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        BlockRayTraceResult rayCastResult = getPlayerPOVHitResult(world, player, containedFluid.isEmpty() ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, world, heldStack, rayCastResult);

        if (eventResult != null)
            return eventResult;

        if (rayCastResult.getType() != RayTraceResult.Type.BLOCK)
            return ActionResult.pass(heldStack);

        BlockPos pos = rayCastResult.getBlockPos();
        Direction side = rayCastResult.getDirection();
        BlockPos adjPos = pos.relative(side);

        if (!world.mayInteract(player, pos) || !player.mayUseItemAt(adjPos, side, heldStack))
            return ActionResult.fail(heldStack);

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!containedFluid.isEmpty())
        {
            BlockPos fluidPos = FluidUtils.canBlockContainFluid(world, pos, state, containedFluid.getFluid()) ? pos : adjPos;

            if (!FluidUtils.tryPlaceFluid(containedFluid, player, world, fluidPos, rayCastResult))
                return ActionResult.fail(heldStack);

            if (!world.isClientSide)
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, fluidPos, heldStack);

            player.awardStat(Stats.ITEM_USED.get(this));
            return ActionResult.sidedSuccess(emptyBucket(handler, heldStack, player), world.isClientSide());
        }

        if (!(block instanceof IBucketPickupHandler))
            return ActionResult.fail(heldStack);

        Fluid fluid = ((IBucketPickupHandler)block).takeLiquid(world, pos, state);

        if (fluid == Fluids.EMPTY)
            return ActionResult.fail(heldStack);

        FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
            return ActionResult.fail(heldStack);

        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.playSound(fluid.getAttributes().getFillSound(), 1.0F, 1.0F);

        ItemStack filledContainer = DrinkHelper.createFilledResult(heldStack, player, handler.getContainer(), false);

        if (!world.isClientSide)
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        return ActionResult.sidedSuccess(filledContainer, world.isClientSide());
    }
}
