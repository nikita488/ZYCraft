package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class BlockUtils
{
    public static void sendBlockUpdated(World level, BlockPos pos, BlockState state)
    {
        if (!level.isClientSide())
            level.sendBlockUpdated(pos, state, state, 0);
    }

    public static void blockChanged(World level, BlockPos pos, BlockState state, boolean immediate)
    {
        if (level.isClientSide())
            level.sendBlockUpdated(pos, state, state, immediate ? Constants.BlockFlags.RERENDER_MAIN_THREAD : 0);
    }
}
