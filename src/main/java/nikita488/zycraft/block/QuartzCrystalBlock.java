package nikita488.zycraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.util.ParticleUtils;

import java.util.Random;

public class QuartzCrystalBlock extends BreakableBlock
{
    public QuartzCrystalBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        ParticleUtils.glowingBlock(state, level, pos, random);
    }
}
