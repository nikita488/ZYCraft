package nikita488.zycraft.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public interface IMultiFluidSource
{
    FluidStack fill(World world, BlockPos pos, Direction side);
}
