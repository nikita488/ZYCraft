package com.nikita488.zycraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;

import java.util.Random;
import java.util.function.Function;

public class QuartzCrystalFeature extends Feature<NoFeatureConfig>
{
    public QuartzCrystalFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> factory)
    {
        super(factory);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
        return true;
        /*if (world.getBlockState(pos) != Blocks.CAVE_AIR.getDefaultState())
            return false;

        for (Direction side : Direction.values())
        {
            BlockPos offset = pos.offset(side);

            if (world.getBlockState(offset).getMaterial() != Material.ROCK)
                continue;

            world.setBlockState(pos, ModBlocks.QUARTZ_CRYSTAL.getDefaultState()
                    .with(QuartzCrystalBlock.FACING, side.getOpposite())
                    .with(QuartzCrystalBlock.AMOUNT, MathHelper.nextInt(rand, 1, 5)), Constants.BlockFlags.BLOCK_UPDATE);
            return true;
        }

        return false;*/
    }
}
