package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.Random;
import java.util.stream.Stream;

public class ClusterPlacement extends FeatureDecorator<ClusterPlacementConfig>
{
    public static final BlockPos.MutableBlockPos POS = new BlockPos.MutableBlockPos();

    public ClusterPlacement(Codec<ClusterPlacementConfig> codec)
    {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecorationContext helper, Random random, ClusterPlacementConfig config, BlockPos pos)
    {
        ObjectArrayList<BlockPos> clusters = new ObjectArrayList<>();

        for (int i = 0; i < config.generationAttempts; i++)
        {
            if (clusters.size() == config.count)
                break;

            int x = pos.getX() + random.nextInt(16);
            int z = pos.getZ() + random.nextInt(16);
            int surfaceHeight = Math.min(helper.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z), helper.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));

            if (surfaceHeight <= 0)
                continue;

            POS.set(x, random.nextInt(surfaceHeight), z);

            if (helper.getBlockState(POS).getBlock() == Blocks.CAVE_AIR)
                clusters.add(POS.immutable());
        }

        return clusters.stream();
    }
}
