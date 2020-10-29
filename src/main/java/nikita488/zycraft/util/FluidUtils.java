package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

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
}
