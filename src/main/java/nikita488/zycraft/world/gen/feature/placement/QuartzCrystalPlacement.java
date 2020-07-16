package nikita488.zycraft.world.gen.feature.placement;

import com.mojang.datafixers.Dynamic;
import nikita488.zycraft.world.gen.feature.placement.config.QuartzCrystalPlacementConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class QuartzCrystalPlacement extends Placement<QuartzCrystalPlacementConfig>
{
    public QuartzCrystalPlacement(Function<Dynamic<?>, ? extends QuartzCrystalPlacementConfig> config)
    {
        super(config);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, QuartzCrystalPlacementConfig config, BlockPos pos)
    {
        ObjectArrayList<BlockPos> crystals = new ObjectArrayList<>();
        BlockPos.Mutable crystalPos = new BlockPos.Mutable();

        for (int i = 0; i < config.generationAttempts; i++)
        {
            if (crystals.size() == config.count)
                break;

            int x = pos.getX() + random.nextInt(16);
            int z = pos.getZ() + random.nextInt(16);
            int surfaceHeight = Math.min(world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, x, z), world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z));
            int y = random.nextInt(surfaceHeight);

            crystalPos.setPos(x, y, z);

            if (world.getBlockState(crystalPos).getBlock() == Blocks.CAVE_AIR)
                crystals.add(crystalPos.toImmutable());
        }

        return crystals.stream();
    }
}
