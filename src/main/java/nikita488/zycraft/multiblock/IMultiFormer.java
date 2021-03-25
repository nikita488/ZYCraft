package nikita488.zycraft.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IMultiFormer
{
    boolean form(World world, BlockPos pos, @Nullable Direction side);
}
