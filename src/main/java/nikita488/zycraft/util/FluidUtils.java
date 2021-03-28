package nikita488.zycraft.util;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class FluidUtils
{
    public static Optional<IFluidHandlerItem> getItemFluidHandler(@Nonnull ItemStack stack)
    {
        if (stack.isEmpty())
            return Optional.empty();

        stack = ItemStackUtils.copy(stack, 1);
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve();
    }

    public static boolean canBlockContainFluid(World world, BlockPos pos, BlockState state, Fluid fluid)
    {
        return state.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)state.getBlock()).canContainFluid(world, pos, state, fluid);
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
        boolean replaceable = blockState.isReplaceable(fluid);
        boolean canContainFluid = blockState.isAir() || replaceable || canBlockContainFluid(world, pos, blockState, fluid);

        if (!canContainFluid)
            return rayCastResult != null && tryPlaceFluid(stack, player, world, rayCastResult.getPos().offset(rayCastResult.getFace()), null);

        if (world.getDimensionType().isUltrawarm() && attributes.doesVaporize(world, pos, stack))
        {
            attributes.vaporize(player, world, pos, stack);
            return true;
        }

        if (canBlockContainFluid(world, pos, blockState, fluid))
        {
            ((ILiquidContainer)blockState.getBlock()).receiveFluid(world, pos, blockState, ((FlowingFluid)fluid).getStillFluidState(false));
            world.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }

        if (!world.isRemote() && replaceable && !blockState.getMaterial().isLiquid())
            world.destroyBlock(pos, true);

        if (!world.setBlockState(pos, attributes.getBlock(world, pos, fluidState), Constants.BlockFlags.DEFAULT_AND_RERENDER) && !blockState.getFluidState().isSource())
            return false;

        world.playSound(player, pos, attributes.getEmptySound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        return true;
    }

    public static boolean voidFluid(BlockState state, World world, BlockPos pos)
    {
        if (state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler)state.getBlock()).pickupFluid(world, pos, state) != Fluids.EMPTY)
            return true;

        if (!(state.getBlock() instanceof FlowingFluidBlock))
        {
            if (state.getMaterial() != Material.OCEAN_PLANT && state.getMaterial() != Material.SEA_GRASS)
                return false;

            Block.spawnDrops(state, world, pos, state.hasTileEntity() ? world.getTileEntity(pos) : null);
        }

        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.DEFAULT);
        return true;
    }

    public static boolean turnLavaIntoBlock(World world, BlockPos pos, FluidState fluidState)
    {
        if (!fluidState.isTagged(FluidTags.LAVA))
            return false;

        Block block = fluidState.isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
        world.setBlockState(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, block.getDefaultState()));
        world.playEvent(Constants.WorldEvents.LAVA_EXTINGUISH, pos, -1);
        return true;
    }

    public static boolean turnLavaIntoBasalt(World world, BlockPos pos, FluidState fluidState)
    {
        if (!fluidState.isTagged(FluidTags.LAVA) || !world.getBlockState(pos.down()).matchesBlock(Blocks.SOUL_SOIL))
            return false;

        world.setBlockState(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, Blocks.BASALT.getDefaultState()));
        world.playEvent(Constants.WorldEvents.LAVA_EXTINGUISH, pos, -1);
        return true;
    }

    public static boolean turnWaterIntoIce(BlockState blockState, World world, BlockPos pos, FluidState fluidState)
    {
        if (!fluidState.isTagged(FluidTags.WATER) || !(blockState.getBlock() instanceof FlowingFluidBlock))
            return false;

        world.setBlockState(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, Blocks.ICE.getDefaultState()));
        return true;
    }
}
