package nikita488.zycraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
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

    public static boolean canPlaceFluid(Level level, BlockPos pos, BlockState state, Fluid fluid)
    {
        return state.getBlock() instanceof LiquidBlockContainer container && container.canPlaceLiquid(level, pos, state, fluid);
    }

    public static boolean tryPlaceFluid(FluidStack stack, @Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hitResult)
    {
        Fluid fluid = stack.getFluid();

        if (fluid instanceof FlowingFluid flowingFluid)
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
                ((LiquidBlockContainer)blockState.getBlock()).placeLiquid(level, pos, blockState, flowingFluid.getSource(false));
                level.playSound(player, pos, attributes.getEmptySound(), SoundSource.BLOCKS, 1F, 1F);
                level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
                return true;
            }

            if (!level.isClientSide() && replaceable && !blockState.getMaterial().isLiquid())
                level.destroyBlock(pos, true);

            if (!level.setBlock(pos, attributes.getBlock(level, pos, fluidState), Block.UPDATE_ALL_IMMEDIATE) && !blockState.getFluidState().isSource())
                return false;

            level.playSound(player, pos, attributes.getEmptySound(), SoundSource.BLOCKS, 1F, 1F);
            level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
            return true;
        }

        return false;
    }

    public static boolean voidFluid(Level level, BlockPos pos, Predicate<FluidState> predicate)
    {
        if (!predicate.test(level.getFluidState(pos)))
            return false;

        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof BucketPickup pickup && !pickup.pickupBlock(level, pos, state).isEmpty())
            return true;

        if (!(state.getBlock() instanceof LiquidBlock))
        {
            Material material = state.getMaterial();

            if (material != Material.WATER_PLANT && material != Material.REPLACEABLE_WATER_PLANT)
                return false;

            Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null);
        }

        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        return true;
    }
}
