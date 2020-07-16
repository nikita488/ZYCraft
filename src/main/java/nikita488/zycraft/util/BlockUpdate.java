package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class BlockUpdate
{
    public static void scheduleTileUpdate(World world, BlockPos pos, BlockState state)
    {
        if (world.isRemote) return;
        world.notifyBlockUpdate(pos, state, state, 0);
    }

    public static void scheduleRenderUpdate(World world, BlockPos pos)
    {
        scheduleRenderUpdate(world, pos, false);
    }

    public static void scheduleRenderUpdate(World world, BlockPos pos, boolean mainThread)
    {
        if (!world.isRemote) return;
        BlockState state = Blocks.AIR.getDefaultState();
        world.notifyBlockUpdate(pos, state, state, mainThread ? Constants.BlockFlags.RERENDER_MAIN_THREAD : 0);
    }
}
