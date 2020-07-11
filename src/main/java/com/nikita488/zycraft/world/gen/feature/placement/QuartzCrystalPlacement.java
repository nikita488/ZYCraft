package com.nikita488.zycraft.world.gen.feature.placement;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class QuartzCrystalPlacement extends Placement<CountRangeConfig>
{
    public QuartzCrystalPlacement(Function<Dynamic<?>, ? extends CountRangeConfig> config)
    {
        super(config);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, CountRangeConfig config, BlockPos pos)
    {
        return IntStream.range(0, config.count).mapToObj((index) ->
        {
            int x = random.nextInt(16) + pos.getX();
            int z = random.nextInt(16) + pos.getZ();
            int y = random.nextInt(world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z));
            return new BlockPos(x, y, z);
        });
    }
}
