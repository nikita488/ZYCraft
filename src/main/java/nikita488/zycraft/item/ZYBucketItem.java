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
        return !player.abilities.isCreativeMode ? new ItemStack(this) : stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack heldStack = player.getHeldItem(hand);
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return ActionResult.resultPass(heldStack);

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        BlockRayTraceResult rayCastResult = rayTrace(world, player, containedFluid.isEmpty() ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, world, heldStack, rayCastResult);

        if (eventResult != null)
            return eventResult;

        if (rayCastResult.getType() != RayTraceResult.Type.BLOCK)
            return ActionResult.resultPass(heldStack);

        BlockPos pos = rayCastResult.getPos();
        Direction side = rayCastResult.getFace();
        BlockPos adjPos = pos.offset(side);

        if (!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(adjPos, side, heldStack))
            return ActionResult.resultFail(heldStack);

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!containedFluid.isEmpty())
        {
            BlockPos fluidPos = FluidUtils.canBlockContainFluid(world, pos, state, containedFluid.getFluid()) ? pos : adjPos;

            if (!FluidUtils.tryPlaceFluid(containedFluid, player, world, fluidPos, rayCastResult))
                return ActionResult.resultFail(heldStack);

            if (!world.isRemote())
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, fluidPos, heldStack);

            player.addStat(Stats.ITEM_USED.get(this));
            return ActionResult.func_233538_a_(emptyBucket(handler, heldStack, player), world.isRemote());
        }

        if (!(block instanceof IBucketPickupHandler))
            return ActionResult.resultFail(heldStack);

        Fluid fluid = ((IBucketPickupHandler)block).pickupFluid(world, pos, state);

        if (fluid == Fluids.EMPTY)
            return ActionResult.resultFail(heldStack);

        FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
            return ActionResult.resultFail(heldStack);

        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        player.addStat(Stats.ITEM_USED.get(this));
        player.playSound(fluid.getAttributes().getFillSound(), 1.0F, 1.0F);

        ItemStack filledContainer = DrinkHelper.fill(heldStack, player, handler.getContainer(), false);

        if (!world.isRemote())
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        return ActionResult.func_233538_a_(filledContainer, world.isRemote());
    }
}
