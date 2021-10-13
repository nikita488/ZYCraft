package nikita488.zycraft.util;

import net.minecraft.block.*;
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

    public static boolean canBlockContainFluid(World world, BlockPos pos, BlockState state, Fluid fluid)
    {
        return state.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)state.getBlock()).canPlaceLiquid(world, pos, state, fluid);
    }

    public static boolean tryPlaceFluid(FluidStack stack, @Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockRayTraceResult rayCastResult)
    {
        Fluid fluid = stack.getFluid();

        if (!(fluid instanceof FlowingFluid))
            return false;

        FluidAttributes attributes = fluid.getAttributes();
        FluidState fluidState = attributes.getStateForPlacement(world, pos, stack);

        if (fluidState.isEmpty() || !fluidState.isSource() || !attributes.canBePlacedInWorld(world, pos, fluidState))
            return false;

        BlockState blockState = world.getBlockState(pos);
        boolean replaceable = blockState.canBeReplaced(fluid);
        boolean canContainFluid = blockState.isAir() || replaceable || canBlockContainFluid(world, pos, blockState, fluid);

        if (!canContainFluid)
            return rayCastResult != null && tryPlaceFluid(stack, player, world, rayCastResult.getBlockPos().relative(rayCastResult.getDirection()), null);

        if (world.dimensionType().ultraWarm() && attributes.doesVaporize(world, pos, stack))
        {
            attributes.vaporize(player, world, pos, stack);
            return true;
        }

        if (canBlockContainFluid(world, pos, blockState, fluid))
        {
            ((ILiquidContainer)blockState.getBlock()).placeLiquid(world, pos, blockState, ((FlowingFluid)fluid).getSource(false));
            world.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1F, 1F);
            return true;
        }

        if (!world.isClientSide() && replaceable && !blockState.getMaterial().isLiquid())
            world.destroyBlock(pos, true);

        if (!world.setBlock(pos, attributes.getBlock(world, pos, fluidState), Constants.BlockFlags.DEFAULT_AND_RERENDER) && !blockState.getFluidState().isSource())
            return false;

        world.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1F, 1F);
        return true;
    }

    public static boolean voidFluid(World world, BlockPos pos, Predicate<FluidState> predicate)
    {
        if (!predicate.test(world.getFluidState(pos)))
            return false;

        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler)state.getBlock()).takeLiquid(world, pos, state) != Fluids.EMPTY)
            return true;

        if (!(state.getBlock() instanceof FlowingFluidBlock))
        {
            Material material = state.getMaterial();

            if (material != Material.WATER_PLANT && material != Material.REPLACEABLE_WATER_PLANT)
                return false;

            Block.dropResources(state, world, pos, state.hasTileEntity() ? world.getBlockEntity(pos) : null);
        }

        world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT);
        return true;
    }
}
