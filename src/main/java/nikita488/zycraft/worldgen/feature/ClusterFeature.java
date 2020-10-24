package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterFeature extends Feature<ClusterFeatureConfig>
{
    private static final Direction[] VALUES = Direction.values();

    public ClusterFeature(Codec<ClusterFeatureConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, ClusterFeatureConfig config)
    {
        List<Direction> possibleSides = new ArrayList<>();
        BlockPos.Mutable adjPos = new BlockPos.Mutable();

        for (Direction side : VALUES)
        {
            if (!config.target.test(world.getBlockState(adjPos.setAndMove(pos, side)), random))
                continue;

            possibleSides.add(side);
        }

        if (possibleSides.isEmpty())
            return false;

        world.setBlockState(pos, ZYBlocks.QUARTZ_CRYSTAL.getDefaultState()
                .with(QuartzCrystalClusterBlock.FACING, possibleSides.get(random.nextInt(possibleSides.size())).getOpposite())
                .with(QuartzCrystalClusterBlock.AMOUNT, MathHelper.nextInt(random, 1, config.size)), Constants.BlockFlags.BLOCK_UPDATE);
        return true;
    }
}
