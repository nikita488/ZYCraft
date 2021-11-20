package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.util.ZYConstants;

import java.util.Random;

public class ClusterFeature extends Feature<ClusterFeatureConfig>
{
    public static final BlockPos.MutableBlockPos RELATIVE_POS = new BlockPos.MutableBlockPos();

    public ClusterFeature(Codec<ClusterFeatureConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ClusterFeatureConfig> context)
    {
        WorldGenLevel level = context.level();
        Random random = context.random();
        BlockPos origin = context.origin();
        ClusterFeatureConfig config = context.config();
        ObjectList<Direction> possibleSides = new ObjectArrayList<>();

        for (Direction side : ZYConstants.DIRECTIONS)
            if (level.hasChunkAt(RELATIVE_POS.setWithOffset(origin, side)) && config.target.test(level.getBlockState(RELATIVE_POS), random))
                possibleSides.add(side);

        if (possibleSides.isEmpty())
            return false;

        level.setBlock(origin, ZYBlocks.QUARTZ_CRYSTAL.getDefaultState()
                .setValue(QuartzCrystalClusterBlock.FACING, possibleSides.get(random.nextInt(possibleSides.size())).getOpposite())
                .setValue(QuartzCrystalClusterBlock.AMOUNT, Mth.nextInt(random, 1, config.size)),
                Block.UPDATE_CLIENTS);
        return true;
    }
}
