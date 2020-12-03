package nikita488.zycraft.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMultiFormer
{
    boolean form(World world, BlockPos pos);
}
