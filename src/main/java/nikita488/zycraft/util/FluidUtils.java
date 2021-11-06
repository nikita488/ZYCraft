package nikita488.zycraft.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class FluidUtils
{
    public static void transferFromTo(IFluidHandler from, IFluidHandler to, int amount)
    {
        transferFromTo(from, to, amount, FluidAttributes.BUCKET_VOLUME);
    }

    public static void transferFromTo(IFluidHandler from, IFluidHandler to, int amount, int maxDrain)
    {
        if (amount > 0)
            tryTransfer(from, to, amount, maxDrain);
        else if (amount < 0)
            tryTransfer(to, from, -amount, maxDrain);
    }

    private static void tryTransfer(IFluidHandler from, IFluidHandler to, int amount, int maxDrain)
    {
        FluidStack drained = from.drain(Math.min(amount, maxDrain), IFluidHandler.FluidAction.SIMULATE);
        int filled = to.fill(drained, IFluidHandler.FluidAction.SIMULATE);

        if (filled <= 0)
            return;

        drained = from.drain(filled, IFluidHandler.FluidAction.EXECUTE);
        to.fill(drained, IFluidHandler.FluidAction.EXECUTE);
    }

    public static Optional<IFluidHandlerItem> getItemFluidHandler(ItemStack stack)
    {
        if (stack.isEmpty())
            return Optional.empty();

        stack = ItemStackUtils.copy(stack, 1);
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve();
    }

    public static boolean canPlaceFluid(World level, BlockPos pos, BlockState state, Fluid fluid)
    {
        return state.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)state.getBlock()).canPlaceLiquid(level, pos, state, fluid);
    }

    public static boolean tryPlaceFluid(FluidStack stack, @Nullable PlayerEntity player, World level, BlockPos pos, @Nullable BlockRayTraceResult hitResult)
    {
        Fluid fluid = stack.getFluid();

        if (fluid instanceof FlowingFluid)
        {
            FluidAttributes attributes = fluid.getAttributes();
            FluidState fluidState = attributes.getStateForPlacement(level, pos, stack);

            if (fluidState.isEmpty() || !fluidState.isSource() || !attributes.canBePlacedInWorld(level, pos, fluidState))
                return false;

            BlockState blockState = level.getBlockState(pos);
            boolean replaceable = blockState.canBeReplaced(fluid);
            boolean canPlace = blockState.isAir() || replaceable || canPlaceFluid(level, pos, blockState, fluid);

            if (!canPlace)
                return hitResult != null && tryPlaceFluid(stack, player, level, hitResult.getBlockPos().relative(hitResult.getDirection()), null);

            if (level.dimensionType().ultraWarm() && attributes.doesVaporize(level, pos, stack))
            {
                attributes.vaporize(player, level, pos, stack);
                return true;
            }

            if (canPlaceFluid(level, pos, blockState, fluid))
            {
                ((ILiquidContainer) blockState.getBlock()).placeLiquid(level, pos, blockState, ((FlowingFluid) fluid).getSource(false));
                level.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1F, 1F);
                return true;
            }

            if (!level.isClientSide() && replaceable && !blockState.getMaterial().isLiquid())
                level.destroyBlock(pos, true);

            if (!level.setBlock(pos, attributes.getBlock(level, pos, fluidState), Constants.BlockFlags.DEFAULT_AND_RERENDER) && !blockState.getFluidState().isSource())
                return false;

            level.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1F, 1F);
            return true;
        }

        return false;
    }

    public static boolean voidFluid(World level, BlockPos pos, Predicate<FluidState> predicate)
    {
        if (!predicate.test(level.getFluidState(pos)))
            return false;

        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler)state.getBlock()).takeLiquid(level, pos, state) != Fluids.EMPTY)
            return true;

        if (!(state.getBlock() instanceof FlowingFluidBlock))
        {
            Material material = state.getMaterial();

            if (material != Material.WATER_PLANT && material != Material.REPLACEABLE_WATER_PLANT)
                return false;

            Block.dropResources(state, level, pos, state.hasTileEntity() ? level.getBlockEntity(pos) : null);
        }

        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        return true;
    }
}
