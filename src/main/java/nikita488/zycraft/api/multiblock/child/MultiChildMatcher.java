package nikita488.zycraft.api.multiblock.child;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.EnumSet;

public class MultiChildMatcher implements IMultiChildMatcher
{
    private final EnumSet<MultiChildMaterial> materials;

    private MultiChildMatcher(MultiChildMaterial first, MultiChildMaterial... rest)
    {
        this.materials = EnumSet.of(first, rest);
    }

    public static MultiChildMatcher of(MultiChildMaterial first, MultiChildMaterial... rest)
    {
        return new MultiChildMatcher(first, rest);
    }

    @Override
    public boolean matches(IBlockReader world, BlockPos pos)
    {
        return materials.contains(MultiChildMaterial.fromState(world, pos));
    }
}
