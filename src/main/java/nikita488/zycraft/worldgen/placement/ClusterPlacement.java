package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class ClusterPlacement extends Placement<ClusterPlacementConfig>
{
    public static final BlockPos.Mutable POS = new BlockPos.Mutable();

    public ClusterPlacement(Codec<ClusterPlacementConfig> codec)
    {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper context, Random random, ClusterPlacementConfig config, BlockPos pos)
    {
        ObjectList<BlockPos> clusters = new ObjectArrayList<>();

        for (int i = 0; i < config.generationAttempts; i++)
        {
            if (clusters.size() == config.count)
                break;

            int x = pos.getX() + random.nextInt(16);
            int z = pos.getZ() + random.nextInt(16);
            int surfaceHeight = Math.min(context.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z), context.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z));

            if (surfaceHeight <= 0)
                continue;

            POS.set(x, random.nextInt(surfaceHeight), z);

            if (context.getBlockState(POS).getBlock() == Blocks.CAVE_AIR)
                clusters.add(POS.immutable());
        }

        return clusters.stream();
    }
}
