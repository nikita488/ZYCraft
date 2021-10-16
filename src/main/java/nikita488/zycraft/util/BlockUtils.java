package nikita488.zycraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

public class BlockUtils
{
    public static void sendBlockUpdated(Level level, BlockPos pos, BlockState state)
    {
        if (!level.isClientSide())
            level.sendBlockUpdated(pos, state, state, 0);
    }

    public static void blockChanged(Level level, BlockPos pos, BlockState state, boolean immediate)
    {
        if (level.isClientSide())
            level.sendBlockUpdated(pos, state, state, immediate ? Constants.BlockFlags.RERENDER_MAIN_THREAD : 0);
    }
}
