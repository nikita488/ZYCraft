package nikita488.zycraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

    protected ItemStack emptyBucket(IFluidHandlerItem handler, ItemStack stack, Player player)
    {
        return !player.abilities.instabuild ? new ItemStack(this) : stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack heldStack = player.getItemInHand(hand);
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return InteractionResultHolder.pass(heldStack);

        IFluidHandlerItem handler = capability.get();
        FluidStack containedFluid = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, containedFluid.isEmpty() ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, level, heldStack, hitResult);

        if (eventResult != null)
            return eventResult;

        if (hitResult.getType() != HitResult.Type.BLOCK)
            return InteractionResultHolder.pass(heldStack);

        BlockPos pos = hitResult.getBlockPos();
        Direction side = hitResult.getDirection();
        BlockPos relativePos = pos.relative(side);

        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(relativePos, side, heldStack))
            return InteractionResultHolder.fail(heldStack);

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!containedFluid.isEmpty())
        {
            BlockPos fluidPos = FluidUtils.canBlockContainFluid(level, pos, state, containedFluid.getFluid()) ? pos : relativePos;

            if (!FluidUtils.tryPlaceFluid(containedFluid, player, level, fluidPos, hitResult))
                return InteractionResultHolder.fail(heldStack);

            if (!level.isClientSide())
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, fluidPos, heldStack);

            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(emptyBucket(handler, heldStack, player), level.isClientSide());
        }

        if (!(block instanceof BucketPickup))
            return InteractionResultHolder.fail(heldStack);

        Fluid fluid = ((BucketPickup)block).takeLiquid(level, pos, state);

        if (fluid == Fluids.EMPTY)
            return InteractionResultHolder.fail(heldStack);

        FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != fluidStack.getAmount())
            return InteractionResultHolder.fail(heldStack);

        handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.playSound(fluid.getAttributes().getFillSound(), 1F, 1F);

        ItemStack filledContainer = ItemUtils.createFilledResult(heldStack, player, handler.getContainer(), false);

        if (!level.isClientSide())
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, filledContainer);

        return InteractionResultHolder.sidedSuccess(filledContainer, level.isClientSide());
    }
}
