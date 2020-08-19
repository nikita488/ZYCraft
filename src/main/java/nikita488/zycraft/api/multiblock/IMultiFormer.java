package nikita488.zycraft.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

@FunctionalInterface
public interface IMultiFormer
{
    boolean form(IWorld world, BlockPos interfacePos);
}
