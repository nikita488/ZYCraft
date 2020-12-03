package nikita488.zycraft.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiFluidVoid
{
    int drain(World world, BlockPos pos, Direction side);
}
