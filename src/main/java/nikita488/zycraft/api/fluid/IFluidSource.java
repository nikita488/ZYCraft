package nikita488.zycraft.api.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public interface IFluidSource
{
    FluidStack getFluid(BlockState state, Level level, BlockPos pos, @Nullable Direction side);
}
