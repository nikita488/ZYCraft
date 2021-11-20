package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.init.ZYBlocks;

import java.util.BitSet;
import java.util.Random;

public class ZychoriteVeinFeature extends Feature<ZychoriteVeinConfig>
{
    public ZychoriteVeinFeature(Codec<ZychoriteVeinConfig> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ZychoriteVeinConfig> context)
    {
        Random random = context.random();
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        ZychoriteVeinConfig config = context.config();
        float f = random.nextFloat() * (float)Math.PI;
        float f1 = (float)config.size / 8.0F;
        int i = Mth.ceil(((float)config.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double)pos.getX() + Math.sin((double)f) * (double)f1;
        double d1 = (double)pos.getX() - Math.sin((double)f) * (double)f1;
        double d2 = (double)pos.getZ() + Math.cos((double)f) * (double)f1;
        double d3 = (double)pos.getZ() - Math.cos((double)f) * (double)f1;
        int j = 2;
        double d4 = (double)(pos.getY() + random.nextInt(3) - 2);
        double d5 = (double)(pos.getY() + random.nextInt(3) - 2);
        int k = pos.getX() - Mth.ceil(f1) - i;
        int l = pos.getY() - 2 - i;
        int i1 = pos.getZ() - Mth.ceil(f1) - i;
        int j1 = 2 * (Mth.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for(int l1 = k; l1 <= k + j1; ++l1) {
            for(int i2 = i1; i2 <= i1 + j1; ++i2) {
                if (l <= level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2)) {
                    return this.doPlace(level, random, config, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
                }
            }
        }

        return false;
    }

    protected boolean doPlace(WorldGenLevel level, Random random, ZychoriteVeinConfig config, double p_66536_, double p_66537_, double p_66538_, double p_66539_, double p_66540_, double p_66541_, int p_66542_, int p_66543_, int p_66544_, int p_66545_, int p_66546_)
    {
        int i = 0;
        BitSet bitset = new BitSet(p_66545_ * p_66546_ * p_66545_);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int j = config.size;
        double[] adouble = new double[j * 4];

        for(int k = 0; k < j; ++k) {
            float f = (float)k / (float)j;
            double d0 = Mth.lerp((double)f, p_66536_, p_66537_);
            double d1 = Mth.lerp((double)f, p_66540_, p_66541_);
            double d2 = Mth.lerp((double)f, p_66538_, p_66539_);
            double d3 = random.nextDouble() * (double)j / 16.0D;
            double d4 = ((double)(Mth.sin((float)Math.PI * f) + 1.0F) * d3 + 1.0D) / 2.0D;
            adouble[k * 4 + 0] = d0;
            adouble[k * 4 + 1] = d1;
            adouble[k * 4 + 2] = d2;
            adouble[k * 4 + 3] = d4;
        }

        for(int l3 = 0; l3 < j - 1; ++l3) {
            if (!(adouble[l3 * 4 + 3] <= 0.0D)) {
                for(int i4 = l3 + 1; i4 < j; ++i4) {
                    if (!(adouble[i4 * 4 + 3] <= 0.0D)) {
                        double d8 = adouble[l3 * 4 + 0] - adouble[i4 * 4 + 0];
                        double d10 = adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1];
                        double d12 = adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2];
                        double d14 = adouble[l3 * 4 + 3] - adouble[i4 * 4 + 3];
                        if (d14 * d14 > d8 * d8 + d10 * d10 + d12 * d12) {
                            if (d14 > 0.0D) {
                                adouble[i4 * 4 + 3] = -1.0D;
                            } else {
                                adouble[l3 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        BulkSectionAccess bulksectionaccess = new BulkSectionAccess(level);

        try {
            for(int j4 = 0; j4 < j; ++j4) {
                double d9 = adouble[j4 * 4 + 3];
                if (!(d9 < 0.0D)) {
                    double d11 = adouble[j4 * 4 + 0];
                    double d13 = adouble[j4 * 4 + 1];
                    double d15 = adouble[j4 * 4 + 2];
                    int k4 = Math.max(Mth.floor(d11 - d9), p_66542_);
                    int l = Math.max(Mth.floor(d13 - d9), p_66543_);
                    int i1 = Math.max(Mth.floor(d15 - d9), p_66544_);
                    int j1 = Math.max(Mth.floor(d11 + d9), k4);
                    int k1 = Math.max(Mth.floor(d13 + d9), l);
                    int l1 = Math.max(Mth.floor(d15 + d9), i1);

                    for(int i2 = k4; i2 <= j1; ++i2) {
                        double d5 = ((double)i2 + 0.5D - d11) / d9;
                        if (d5 * d5 < 1.0D) {
                            for(int j2 = l; j2 <= k1; ++j2) {
                                double d6 = ((double)j2 + 0.5D - d13) / d9;
                                if (d5 * d5 + d6 * d6 < 1.0D) {
                                    for(int k2 = i1; k2 <= l1; ++k2) {
                                        double d7 = ((double)k2 + 0.5D - d15) / d9;
                                        if (d5 * d5 + d6 * d6 + d7 * d7 < 1.0D && !level.isOutsideBuildHeight(j2)) {
                                            int l2 = i2 - p_66542_ + (j2 - p_66543_) * p_66545_ + (k2 - p_66544_) * p_66545_ * p_66546_;
                                            if (!bitset.get(l2)) {
                                                bitset.set(l2);
                                                blockpos$mutableblockpos.set(i2, j2, k2);
                                                if (level.ensureCanWrite(blockpos$mutableblockpos)) {
                                                    LevelChunkSection levelchunksection = bulksectionaccess.getSection(blockpos$mutableblockpos);
                                                    if (levelchunksection != LevelChunk.EMPTY_SECTION) {
                                                        int i3 = SectionPos.sectionRelative(i2);
                                                        int j3 = SectionPos.sectionRelative(j2);
                                                        int k3 = SectionPos.sectionRelative(k2);
                                                        BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);

                                                        if (config.target.test(blockstate, random)) {
                                                            Block block = ZYBlocks.ZYCHORITE.get();

                                                            if (random.nextFloat() < config.orePercentage)
                                                                block = ZYBlocks.ZYCHORIUM_ORE.get(ZYType.randomType(random)).get();

                                                            levelchunksection.setBlockState(i3, j3, k3, block.defaultBlockState(), false);
                                                            ++i;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable1) {
            try {
                bulksectionaccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }

            throw throwable1;
        }

        bulksectionaccess.close();
        return i > 0;
    }
}
