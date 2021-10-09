package nikita488.zycraft.api.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IFluidSource
{
    FluidStack getFluid(BlockState state, World world, BlockPos pos, @Nullable Direction side);
}
