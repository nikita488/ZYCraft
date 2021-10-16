package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FireBasinBlock extends Block
{
    public FireBasinBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isFireSource(BlockState state, LevelReader reader, BlockPos pos, Direction side)
    {
        return side == Direction.UP;
    }
}
