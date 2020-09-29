package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClusterFeature extends Feature<NoFeatureConfig>
{
    private static final Direction[] VALUES = Direction.values();

    public ClusterFeature(Codec<NoFeatureConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config)
    {
        List<Direction> possibleSides = new ArrayList<>();
        BlockPos.Mutable adjPos = new BlockPos.Mutable();

        for (Direction side : VALUES)
        {
            if (world.getBlockState(adjPos.setAndMove(pos, side)).getMaterial() != Material.ROCK ||
                    !ZYBlocks.QUARTZ_CRYSTAL_CLUSTER.getDefaultState().with(QuartzCrystalClusterBlock.FACING, side.getOpposite()).isValidPosition(world, adjPos))
                continue;

            possibleSides.add(side);
        }

        if (possibleSides.isEmpty())
            return false;

        world.setBlockState(pos, ZYBlocks.QUARTZ_CRYSTAL_CLUSTER.getDefaultState()
                .with(QuartzCrystalClusterBlock.FACING, possibleSides.get(random.nextInt(possibleSides.size())).getOpposite())
                .with(QuartzCrystalClusterBlock.AMOUNT, MathHelper.nextInt(random, 1, 5)), Constants.BlockFlags.BLOCK_UPDATE);
        return true;
    }
}
