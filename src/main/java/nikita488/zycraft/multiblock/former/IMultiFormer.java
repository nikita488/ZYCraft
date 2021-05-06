package nikita488.zycraft.multiblock.former;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IMultiFormer
{
    default boolean form(World world, BlockPos pos)
    {
        return form(world, pos, null);
    }

    boolean form(World world, BlockPos pos, @Nullable Direction side);
}
