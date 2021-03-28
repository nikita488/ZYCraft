package nikita488.zycraft.api.colorable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public interface IColorable
{
    default int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintIndex)
    {
        return getColor(state, world, pos);
    }

    int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos);

    void setColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int rgb);
}
