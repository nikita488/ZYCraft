package nikita488.zycraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import nikita488.zycraft.enums.ZyType;
import nikita488.zycraft.init.ModBlocks;
import nikita488.zycraft.world.gen.feature.сonfig.ZychoriteVeinFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class ZychoriteVeinFeature extends Feature<ZychoriteVeinFeatureConfig>
{
    public ZychoriteVeinFeature(Function<Dynamic<?>, ? extends ZychoriteVeinFeatureConfig> factory)
    {
        super(factory);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ZychoriteVeinFeatureConfig config)
    {
        float f = rand.nextFloat() * (float)Math.PI;
        float f1 = (float)config.size / 8.0F;
        int i = MathHelper.ceil(((float)config.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double)((float)pos.getX() + MathHelper.sin(f) * f1);
        double d1 = (double)((float)pos.getX() - MathHelper.sin(f) * f1);
        double d2 = (double)((float)pos.getZ() + MathHelper.cos(f) * f1);
        double d3 = (double)((float)pos.getZ() - MathHelper.cos(f) * f1);
        int j = 2;
        double d4 = (double)(pos.getY() + rand.nextInt(3) - 2);
        double d5 = (double)(pos.getY() + rand.nextInt(3) - 2);
        int k = pos.getX() - MathHelper.ceil(f1) - i;
        int l = pos.getY() - 2 - i;
        int i1 = pos.getZ() - MathHelper.ceil(f1) - i;
        int j1 = 2 * (MathHelper.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for(int l1 = k; l1 <= k + j1; l1++)
            for(int i2 = i1; i2 <= i1 + j1; i2++)
                if (l <= world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, l1, i2))
                    return doPlace(world, rand, config, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);

        return false;
    }

    private boolean doPlace(IWorld world, Random random, ZychoriteVeinFeatureConfig config, double p_207803_4_, double p_207803_6_, double p_207803_8_, double p_207803_10_, double p_207803_12_, double p_207803_14_, int p_207803_16_, int p_207803_17_, int p_207803_18_, int p_207803_19_, int p_207803_20_)
    {
        int blockCount = 0;
        BitSet bitset = new BitSet(p_207803_19_ * p_207803_20_ * p_207803_19_);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        double[] adouble = new double[config.size * 4];

        for(int i = 0; i < config.size; i++)
        {
            float f = (float)i / (float)config.size;
            double d0 = MathHelper.lerp((double)f, p_207803_4_, p_207803_6_);
            double d2 = MathHelper.lerp((double)f, p_207803_12_, p_207803_14_);
            double d4 = MathHelper.lerp((double)f, p_207803_8_, p_207803_10_);
            double d6 = random.nextDouble() * (double)config.size / 16.0D;
            double d7 = ((double)(MathHelper.sin((float)Math.PI * f) + 1.0F) * d6 + 1.0D) / 2.0D;
            adouble[i * 4] = d0;
            adouble[i * 4 + 1] = d2;
            adouble[i * 4 + 2] = d4;
            adouble[i * 4 + 3] = d7;
        }

        for(int i = 0; i < config.size - 1; i++)
        {
            if (adouble[i * 4 + 3] <= 0.0D)
                continue;

            for(int j = i + 1; j < config.size; j++)
            {
                if (adouble[j * 4 + 3] <= 0.0D)
                    continue;

                double d12 = adouble[i * 4] - adouble[j * 4];
                double d13 = adouble[i * 4 + 1] - adouble[j * 4 + 1];
                double d14 = adouble[i * 4 + 2] - adouble[j * 4 + 2];
                double d15 = adouble[i * 4 + 3] - adouble[j * 4 + 3];

                if (d15 * d15 <= d12 * d12 + d13 * d13 + d14 * d14)
                    continue;

                if (d15 > 0.0D)
                    adouble[j * 4 + 3] = -1.0D;
                else
                    adouble[i * 4 + 3] = -1.0D;
            }
        }

        for(int i = 0; i < config.size; i++)
        {
            double d11 = adouble[i * 4 + 3];

            if (d11 < 0.0D)
                continue;

            double d1 = adouble[i * 4];
            double d3 = adouble[i * 4 + 1];
            double d5 = adouble[i * 4 + 2];
            int k = Math.max(MathHelper.floor(d1 - d11), p_207803_16_);
            int k3 = Math.max(MathHelper.floor(d3 - d11), p_207803_17_);
            int l = Math.max(MathHelper.floor(d5 - d11), p_207803_18_);
            int i1 = Math.max(MathHelper.floor(d1 + d11), k);
            int j1 = Math.max(MathHelper.floor(d3 + d11), k3);
            int k1 = Math.max(MathHelper.floor(d5 + d11), l);

            for(int x = k; x <= i1; x++)
            {
                double d8 = ((double)x + 0.5D - d1) / d11;

                if (d8 * d8 >= 1.0D)
                    continue;

                for (int y = k3; y <= j1; y++)
                {
                    double d9 = ((double)y + 0.5D - d3) / d11;

                    if (d8 * d8 + d9 * d9 >= 1.0D)
                        continue;

                    for (int z = l; z <= k1; z++)
                    {
                        double d10 = ((double)z + 0.5D - d5) / d11;

                        if (d8 * d8 + d9 * d9 + d10 * d10 >= 1.0D)
                            continue;

                        int k2 = x - p_207803_16_ + (y - p_207803_17_) * p_207803_19_ + (z - p_207803_18_) * p_207803_19_ * p_207803_20_;

                        if (bitset.get(k2))
                            continue;

                        bitset.set(k2);
                        pos.setPos(x, y, z);

                        if (!config.replaceableBlock.getTargetBlockPredicate().test(world.getBlockState(pos)))
                            continue;

                        setVeinBlock(world, pos, random, config);
                        blockCount++;
                    }
                }
            }
        }

        return blockCount > 0;
    }

    public void setVeinBlock(IWorld world, BlockPos pos, Random random, ZychoriteVeinFeatureConfig config)
    {
        Block block = ModBlocks.ZYCHORITE.get();

        if (random.nextFloat() < config.orePercentage)
            block = ModBlocks.ZYCHORIUM_ORE.get(ZyType.random(random)).get();

        world.setBlockState(pos, block.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
    }
}
