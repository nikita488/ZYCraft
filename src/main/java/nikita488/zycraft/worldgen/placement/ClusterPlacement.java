package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class ClusterPlacement extends Placement<ClusterConfig>
{
    public ClusterPlacement(Codec<ClusterConfig> codec)
    {
        super(codec);
    }

    @Override
    public Stream<BlockPos> func_241857_a(WorldDecoratingHelper helper, Random random, ClusterConfig config, BlockPos pos)
    {
        ObjectArrayList<BlockPos> clusters = new ObjectArrayList<>();
        BlockPos.Mutable clusterPos = new BlockPos.Mutable();

        for (int i = 0; i < config.generationAttempts; i++)
        {
            if (clusters.size() == config.count)
                break;

            int x = pos.getX() + random.nextInt(16);
            int z = pos.getZ() + random.nextInt(16);
            int surfaceHeight = Math.min(helper.func_242893_a(Heightmap.Type.OCEAN_FLOOR_WG, x, z), helper.func_242893_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z));
            int y = random.nextInt(surfaceHeight);

            clusterPos.setPos(x, y, z);

            if (helper.func_242894_a(clusterPos).getBlock() == Blocks.CAVE_AIR)
                clusters.add(clusterPos.toImmutable());
        }

        return clusters.stream();
    }
}
