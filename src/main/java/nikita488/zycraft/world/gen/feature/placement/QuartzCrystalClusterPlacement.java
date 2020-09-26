package nikita488.zycraft.world.gen.feature.placement;

import com.mojang.datafixers.Dynamic;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.Placement;
import nikita488.zycraft.world.gen.feature.placement.config.QuartzCrystalClusterPlacementConfig;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class QuartzCrystalClusterPlacement extends Placement<QuartzCrystalClusterPlacementConfig>
{
    public QuartzCrystalClusterPlacement(Function<Dynamic<?>, ? extends QuartzCrystalClusterPlacementConfig> config)
    {
        super(config);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, QuartzCrystalClusterPlacementConfig config, BlockPos pos)
    {
        ObjectArrayList<BlockPos> clusters = new ObjectArrayList<>();
        BlockPos.Mutable clusterPos = new BlockPos.Mutable();

        for (int i = 0; i < config.generationAttempts; i++)
        {
            if (clusters.size() == config.count)
                break;

            int x = pos.getX() + random.nextInt(16);
            int z = pos.getZ() + random.nextInt(16);
            int surfaceHeight = Math.min(world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z), world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z));
            int y = random.nextInt(surfaceHeight);

            clusterPos.setPos(x, y, z);

            if (world.getBlockState(clusterPos).getBlock() == Blocks.CAVE_AIR)
                clusters.add(clusterPos.toImmutable());
        }

        return clusters.stream();
    }
}
