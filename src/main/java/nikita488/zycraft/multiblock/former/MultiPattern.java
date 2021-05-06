package nikita488.zycraft.multiblock.former;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.util.Cuboid6i;

public class MultiPattern
{
    private final int[][][] pattern;
    private final IMultiChildMatcher[] matchers;

    private MultiPattern(int[][][] pattern, IMultiChildMatcher[] matchers)
    {
        this.pattern = pattern;
        this.matchers = matchers;
    }

    public static MultiPattern.Builder builder(int width, int height, int depth)
    {
        return new Builder(width, height, depth);
    }

    public boolean matches(IBlockReader world, BlockPos basePos)
    {
        BlockPos.Mutable matchingPos = basePos.toMutable();

        for (int i = 0; i < width(); i++)
        {
            for (int j = 0; j < height(); j++)
            {
                for (int k = 0; k < depth(); k++)
                {
                    int matcherIndex = pattern[i][j][k];

                    if (matcherIndex >= 0 && !matchers[matcherIndex].matches(world, matchingPos.setAndOffset(basePos, i, j, k)))
                        return false;
                }
            }
        }

        return true;
    }

    public int width()
    {
        return pattern.length;
    }

    public int height()
    {
        return pattern[0].length;
    }

    public int depth()
    {
        return pattern[0][0].length;
    }

    public static final class Builder
    {
        private static final int NULL = -2;
        private static final int ALWAYS_MATCHES = -1;
        private final int[][][] pattern;
        private final ObjectList<IMultiChildMatcher> matchers = new ObjectArrayList<>();

        private Builder(int width, int height, int depth)
        {
            this.pattern = new int[width][height][depth];

            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    for (int z = 0; z < depth; z++)
                        this.pattern[x][y][z] = NULL;
        }

        private int indexOf(IMultiChildMatcher matcher)
        {
            if (matcher == IMultiChildMatcher.ALWAYS_MATCHES)
                return ALWAYS_MATCHES;

            int matcherIndex = matchers.indexOf(matcher);

            if (matcherIndex != -1)
                return matcherIndex;

            matchers.add(matcher);
            return matchers.size() - 1;
        }

        public Builder cuboid(IMultiChildMatcher matcher)
        {
            return cuboid(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder cuboid(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return cuboid(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder cuboid(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            int matcherIndex = indexOf(matcher);

            for (int x = x1; x <= x2; x++)
                for (int y = y1; y <= y2; y++)
                    for (int z = z1; z <= z2; z++)
                        this.pattern[x][y][z] = matcherIndex;
            return this;
        }

        public Builder corners(IMultiChildMatcher matcher)
        {
            return corners(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder corners(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return corners(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder corners(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            int matcherIndex = indexOf(matcher);
            this.pattern[x1][y1][z1] = matcherIndex;
            this.pattern[x1][y1][z2] = matcherIndex;
            this.pattern[x1][y2][z1] = matcherIndex;
            this.pattern[x1][y2][z2] = matcherIndex;
            this.pattern[x2][y1][z1] = matcherIndex;
            this.pattern[x2][y1][z2] = matcherIndex;
            this.pattern[x2][y2][z1] = matcherIndex;
            this.pattern[x2][y2][z2] = matcherIndex;
            return this;
        }

        public Builder edges(IMultiChildMatcher matcher)
        {
            return edges(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder edges(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return edges(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder edges(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            int matcherIndex = indexOf(matcher);

            for (int x = x1 + 1; x <= x2 - 1; x++)
            {
                this.pattern[x][y1][z1] = matcherIndex;
                this.pattern[x][y1][z2] = matcherIndex;
                this.pattern[x][y2][z1] = matcherIndex;
                this.pattern[x][y2][z2] = matcherIndex;
            }

            for (int y = y1 + 1; y <= y2 - 1; y++)
            {
                this.pattern[x1][y][z1] = matcherIndex;
                this.pattern[x1][y][z2] = matcherIndex;
                this.pattern[x2][y][z1] = matcherIndex;
                this.pattern[x2][y][z2] = matcherIndex;
            }

            for (int z = z1 + 1; z <= z2 - 1; z++)
            {
                this.pattern[x1][y1][z] = matcherIndex;
                this.pattern[x1][y2][z] = matcherIndex;
                this.pattern[x2][y1][z] = matcherIndex;
                this.pattern[x2][y2][z] = matcherIndex;
            }

            return this;
        }

        public Builder frame(IMultiChildMatcher matcher)
        {
            return frame(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder frame(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return frame(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder frame(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            corners(x1, y1, z1, x2, y2, z2, matcher);
            edges(x1, y1, z1, x2, y2, z2, matcher);
            return this;
        }

        public Builder faces(IMultiChildMatcher matcher)
        {
            return faces(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder faces(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return faces(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder faces(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            cuboid(x1 + 1, y1, z1 + 1, x2 - 1, y1, z2 - 1, matcher);
            cuboid(x1 + 1, y2, z1 + 1, x2 - 1, y2, z2 - 1, matcher);
            cuboid(x1 + 1, y1 + 1, z1, x2 - 1, y2 - 1, z1, matcher);
            cuboid(x1 + 1, y1 + 1, z2, x2 - 1, y2 - 1, z2, matcher);
            cuboid(x1, y1 + 1, z1 + 1, x1, y2 - 1, z2 - 1, matcher);
            cuboid(x2, y1 + 1, z1 + 1, x2, y2 - 1, z2 - 1, matcher);
            return this;
        }

        public Builder shell(IMultiChildMatcher matcher)
        {
            return shell(0, 0, 0, width() - 1, height() - 1, depth() - 1, matcher);
        }

        public Builder shell(Cuboid6i cuboid, IMultiChildMatcher matcher)
        {
            return shell(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
        }

        public Builder shell(int x1, int y1, int z1, int x2, int y2, int z2, IMultiChildMatcher matcher)
        {
            frame(x1, y1, z1, x2, y2, z2, matcher);
            faces(x1, y1, z1, x2, y2, z2, matcher);
            return this;
        }

        public MultiPattern build()
        {
            return new MultiPattern(pattern, matchers.toArray(new IMultiChildMatcher[0]));
        }

        private int width()
        {
            return pattern.length;
        }

        private int height()
        {
            return pattern[0].length;
        }

        private int depth()
        {
            return pattern[0][0].length;
        }
    }
}
