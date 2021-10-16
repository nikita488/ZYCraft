package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.util.ParticleUtils;

import java.util.Random;

public class QuartzCrystalBlock extends HalfTransparentBlock
{
    public QuartzCrystalBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
    {
        ParticleUtils.glowingBlock(state, level, pos, random);
    }
}
