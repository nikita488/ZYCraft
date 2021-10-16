package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraftforge.common.util.Constants;
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
        WorldGenLevel world = context.level();
        Random random = context.random();
        BlockPos origin = context.origin();
        ClusterFeatureConfig config = context.config();
        ObjectList<Direction> possibleSides = new ObjectArrayList<>();

        for (Direction side : ZYConstants.DIRECTIONS)
            if (config.target.test(world.getBlockState(RELATIVE_POS.setWithOffset(origin, side)), random))
                possibleSides.add(side);

        if (possibleSides.isEmpty())
            return false;

        world.setBlock(origin, ZYBlocks.QUARTZ_CRYSTAL.getDefaultState()
                .setValue(QuartzCrystalClusterBlock.FACING, possibleSides.get(random.nextInt(possibleSides.size())).getOpposite())
                .setValue(QuartzCrystalClusterBlock.AMOUNT, Mth.nextInt(random, 1, config.size)),
                Constants.BlockFlags.BLOCK_UPDATE);
        return true;
    }
}
