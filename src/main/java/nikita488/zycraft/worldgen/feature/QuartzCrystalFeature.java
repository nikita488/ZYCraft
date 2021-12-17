package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.block.QuartzCrystalClusterBlock;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.util.ZYConstants;

import java.util.Random;

public class QuartzCrystalFeature extends Feature<QuartzCrystalFeatureConfig>
{
    private static final BlockPos.Mutable RELATIVE_POS = new BlockPos.Mutable();

    public QuartzCrystalFeature(Codec<QuartzCrystalFeatureConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader level, ChunkGenerator generator, Random random, BlockPos pos, QuartzCrystalFeatureConfig config)
    {
        ObjectList<Direction> possibleSides = new ObjectArrayList<>();

        for (Direction side : ZYConstants.DIRECTIONS)
            if (level.hasChunkAt(RELATIVE_POS.setWithOffset(pos, side)) && config.target.test(level.getBlockState(RELATIVE_POS), random))
                possibleSides.add(side);

        if (possibleSides.isEmpty())
            return false;

        level.setBlock(pos, ZYBlocks.QUARTZ_CRYSTAL.getDefaultState()
                .setValue(QuartzCrystalClusterBlock.FACING, possibleSides.get(random.nextInt(possibleSides.size())).getOpposite())
                .setValue(QuartzCrystalClusterBlock.AMOUNT, MathHelper.nextInt(random, 1, config.size)),
                Constants.BlockFlags.BLOCK_UPDATE);
        return true;
    }
}
