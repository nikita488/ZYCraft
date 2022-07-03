package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.util.ZYConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuartzCrystalFeature extends Feature<QuartzCrystalConfiguration>
{
    public QuartzCrystalFeature(Codec<QuartzCrystalConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<QuartzCrystalConfiguration> context)
    {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        QuartzCrystalConfiguration config = context.config();
        BlockState state = level.getBlockState(origin);

        if (!state.isAir())
            return false;

        BlockPos.MutableBlockPos relativePos = origin.mutable();
        List<Direction> directions = Arrays.asList(ZYConstants.DIRECTIONS);

        Collections.shuffle(directions);

        for (Direction direction : directions)
        {
            BlockState relativeState = level.getBlockState(relativePos.setWithOffset(origin, direction));

            if (!config.canBePlacedOn.test(relativeState, random))
                continue;

            state = ZYBlocks.QUARTZ_CRYSTAL.getDefaultState()
                    .setValue(QuartzCrystalClusterBlock.FACING, direction.getOpposite())
                    .setValue(QuartzCrystalClusterBlock.AMOUNT, Mth.nextInt(random, 1, config.maxCrystals));

            level.setBlock(origin, state, Block.UPDATE_ALL);
            level.getChunk(origin).markPosForPostprocessing(origin);
            return true;
        }

        return false;
    }
}
