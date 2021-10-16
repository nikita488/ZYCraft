package nikita488.zycraft.multiblock.former;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IMultiFormer
{
    default boolean form(BlockState interfaceState, Level level, BlockPos interfacePos)
    {
        return form(interfaceState, level, interfacePos, null);
    }

    boolean form(BlockState interfaceState, Level level, BlockPos interfacePos, @Nullable Direction formingSide);
}
