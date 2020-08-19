package nikita488.zycraft.api.multiblock.child;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

@FunctionalInterface
public interface IMultiChildMatcher
{
    boolean matches(IBlockReader world, BlockPos pos);
}
