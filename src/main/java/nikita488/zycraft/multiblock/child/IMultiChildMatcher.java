package nikita488.zycraft.multiblock.child;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface IMultiChildMatcher
{
    IMultiChildMatcher ALWAYS_MATCHES = (world, pos) -> true;

    boolean matches(BlockGetter getter, BlockPos pos);
}
