package nikita488.zycraft.multiblock.former;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IMultiFormer
{
    default boolean form(BlockState interfaceState, World world, BlockPos interfacePos)
    {
        return form(interfaceState, world, interfacePos, null);
    }

    boolean form(BlockState interfaceState, World world, BlockPos interfacePos, @Nullable Direction formingSide);
}
