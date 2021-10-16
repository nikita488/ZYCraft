package nikita488.zycraft.multiblock.child;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

@FunctionalInterface
public interface IMultiChildMatcher
{
    IMultiChildMatcher ALWAYS_MATCHES = (world, pos) -> true;

    boolean matches(IBlockReader getter, BlockPos pos);
}
