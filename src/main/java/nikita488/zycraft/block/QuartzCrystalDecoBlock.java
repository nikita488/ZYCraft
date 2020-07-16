package nikita488.zycraft.block;

import nikita488.zycraft.util.ParticleSpawn;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class QuartzCrystalDecoBlock extends BreakableBlock
{
    public QuartzCrystalDecoBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        ParticleSpawn.glowingBlock(state, world, pos, rand);
    }
}
