package nikita488.zycraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class QuartzCrystalClusterFeature extends Feature<NoFeatureConfig>
{
    private static final Direction[] VALUES = Direction.values();

    public QuartzCrystalClusterFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> factory)
    {
        super(factory);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        List<Direction> possibleSides = new ArrayList<>();
        BlockPos.Mutable adjPos = new BlockPos.Mutable();

        for (Direction side : VALUES)
        {
            if (world.getBlockState(adjPos.setPos(pos).move(side)).getMaterial() != Material.ROCK ||
                    !ZYBlocks.QUARTZ_CRYSTAL_CLUSTER.getDefaultState().with(QuartzCrystalClusterBlock.FACING, side.getOpposite()).isValidPosition(world, adjPos))
                continue;

            possibleSides.add(side);
        }

        if (possibleSides.isEmpty())
            return false;

        world.setBlockState(pos, ZYBlocks.QUARTZ_CRYSTAL_CLUSTER.getDefaultState()
                .with(QuartzCrystalClusterBlock.FACING, possibleSides.get(rand.nextInt(possibleSides.size())).getOpposite())
                .with(QuartzCrystalClusterBlock.AMOUNT, MathHelper.nextInt(rand, 1, 5)), Constants.BlockFlags.BLOCK_UPDATE);
        return true;
    }
}
