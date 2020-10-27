package nikita488.zycraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.items.ItemHandlerHelper;
import nikita488.zycraft.dispenser.ZYBucketDispenseItemBehavior;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class ZYBucketItem extends ZYFluidContainerItem
{
    public ZYBucketItem(Properties properties)
    {
        this(properties, FluidAttributes.BUCKET_VOLUME);
    }

    public ZYBucketItem(Properties properties, int capacity)
    {
        super(properties, capacity);
        DispenserBlock.registerDispenseBehavior(this, ZYBucketDispenseItemBehavior.INSTANCE);
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if (stack.hasTag() && stack.getTag().contains(FluidHandlerItemStackSimple.FLUID_NBT_KEY, Constants.NBT.TAG_COMPOUND))
            return 1;

        return super.getItemStackLimit(stack);
    }

    private static boolean canBlockContainFluid(World world, BlockPos pos, BlockState state, Fluid fluid)
    {
        return state.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)state.getBlock()).canContainFluid(world, pos, state, fluid);
    }

    protected ItemStack emptyBucket(IFluidHandlerItem handler, ItemStack stack, PlayerEntity player)
    {
        return !player.abilities.isCreativeMode ? new ItemStack(this) : stack;
    }

    public static boolean tryPlaceFluid(Fluid fluid, @Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockRayTraceResult rayCastResult)
    {
        if (!(fluid instanceof FlowingFluid))
            return false;

        BlockState state = world.getBlockState(pos);
        boolean replaceable = state.isReplaceable(fluid);
        boolean canContainFluid = state.isAir() || replaceable || canBlockContainFluid(world, pos, state, fluid);

        if (!canContainFluid)
            return rayCastResult != null && tryPlaceFluid(fluid, player, world, rayCastResult.getPos().offset(rayCastResult.getFace()), null);

        if (world.getDimensionType().isUltrawarm() && fluid.isIn(FluidTags.WATER))
        {
            Random random = world.getRandom();

            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);

            for (int i = 0; i < 8; i++)
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0, 0, 0);

            return true;
        }

        if (canBlockContainFluid(world, pos, state, fluid))
        {
            ((ILiquidContainer)state.getBlock()).receiveFluid(world, pos, state, ((FlowingFluid)fluid).getStillFluidState(false));
            world.playSound(player, pos, fluid.getAttributes().getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        if (!world.isRemote && replaceable && !state.getMaterial().isLiquid())
            world.destroyBlock(pos, true);

        if (!world.setBlockState(pos, fluid.getDefaultState().getBlockState(), 11) && !state.getFluidState().isSource())
            return false;

        world.playSound(player, pos, fluid.getAttributes().getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack heldStack = player.getHeldItem(hand);
        Optional<IFluidHandlerItem> capability = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldStack, 1)).resolve();

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
            Fluid fluid = containedFluid.getFluid();
            BlockPos fluidPos = canBlockContainFluid(world, pos, state, fluid) ? pos : adjPos;

            if (!tryPlaceFluid(fluid, player, world, fluidPos, rayCastResult))
                return ActionResult.resultFail(heldStack);

            if (player instanceof ServerPlayerEntity)
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

        if (!world.isRemote)
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        return ActionResult.func_233538_a_(filledContainer, world.isRemote());
    }
}
