package nikita488.zycraft.multiblock.child;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.multiblock.IMultiChildMatcher;
import nikita488.zycraft.multiblock.MultiChildType;

import java.util.EnumSet;

public class MultiChildMatcher implements IMultiChildMatcher
{
    public static final MultiChildMatcher HARD_AND_GLASS = MultiChildMatcher.of(MultiChildType.HARD, MultiChildType.GLASS);

    private final EnumSet<MultiChildType> types;

    private MultiChildMatcher(EnumSet<MultiChildType> types)
    {
        this.types = types;
    }

    public static MultiChildMatcher of(MultiChildType first, MultiChildType... rest)
    {
        return new MultiChildMatcher(EnumSet.of(first, rest));
    }

    @Override
    public boolean matches(IBlockReader world, BlockPos pos)
    {
        return types.contains(MultiChildType.fromState(world, pos));
    }
}
