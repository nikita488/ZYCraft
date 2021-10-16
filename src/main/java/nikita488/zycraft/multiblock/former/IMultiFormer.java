package nikita488.zycraft.multiblock.former;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IMultiFormer
{
    default boolean form(BlockState interfaceState, World level, BlockPos interfacePos)
    {
        return form(interfaceState, level, interfacePos, null);
    }

    boolean form(BlockState interfaceState, World level, BlockPos interfacePos, @Nullable Direction formingSide);
}
