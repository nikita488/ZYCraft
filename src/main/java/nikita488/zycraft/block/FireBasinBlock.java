package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class FireBasinBlock extends Block
{
    public FireBasinBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader reader, BlockPos pos, Direction side)
    {
        return side == Direction.UP;
    }
}
