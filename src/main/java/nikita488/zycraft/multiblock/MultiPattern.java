package nikita488.zycraft.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.multiblock.tile.MultiChildTile;

import java.io.PrintStream;

public class MultiPattern
{
    private final int width, height, depth;
    private final int[][][] pattern;
    private IMultiChildMatcher[] matchers;

    public MultiPattern(int width, int height, int depth, IMultiChildMatcher[] matchers)
    {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.pattern = new int[width][height][depth];
        this.matchers = matchers;
        setCuboidMatcher(0, 0, 0, width - 1, height - 1, depth - 1, -2);
    }

    public void setCuboidMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                    this.pattern[x][y][z] = id;
    }

    public void setFacesMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        setCuboidMatcher(x1 + 1, y1, z1 + 1, x2 - 1, y1, z2 - 1, id);
        setCuboidMatcher(x1 + 1, y2, z1 + 1, x2 - 1, y2, z2 - 1, id);
        setCuboidMatcher(x1 + 1, y1 + 1, z1, x2 - 1, y2 - 1, z1, id);
        setCuboidMatcher(x1 + 1, y1 + 1, z2, x2 - 1, y2 - 1, z2, id);
        setCuboidMatcher(x1, y1 + 1, z1 + 1, x1, y2 - 1, z2 - 1, id);
        setCuboidMatcher(x2, y1 + 1, z1 + 1, x2, y2 - 1, z2 - 1, id);
    }

    public void setCornersMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        this.pattern[x1][y1][z1] = id;
        this.pattern[x1][y1][z2] = id;
        this.pattern[x1][y2][z1] = id;
        this.pattern[x1][y2][z2] = id;
        this.pattern[x2][y1][z1] = id;
        this.pattern[x2][y1][z2] = id;
        this.pattern[x2][y2][z1] = id;
        this.pattern[x2][y2][z2] = id;
    }

    public void setEdgesMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        for (int x = x1 + 1; x <= x2 - 1; x++)
        {
            this.pattern[x][y1][z1] = id;
            this.pattern[x][y1][z2] = id;
            this.pattern[x][y2][z1] = id;
            this.pattern[x][y2][z2] = id;
        }

        for (int y = y1 + 1; y <= y2 - 1; y++)
        {
            this.pattern[x1][y][z1] = id;
            this.pattern[x1][y][z2] = id;
            this.pattern[x2][y][z1] = id;
            this.pattern[x2][y][z2] = id;
        }

        for (int z = z1 + 1; z <= z2 - 1; z++)
        {
            this.pattern[x1][y1][z] = id;
            this.pattern[x1][y2][z] = id;
            this.pattern[x2][y1][z] = id;
            this.pattern[x2][y2][z] = id;
        }
    }

    public void setFrameMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        setCornersMatcher(x1, y1, z1, x2, y2, z2, id);
        setEdgesMatcher(x1, y1, z1, x2, y2, z2, id);
    }

    public void setShellMatcher(int x1, int y1, int z1, int x2, int y2, int z2, int id)
    {
        setFrameMatcher(x1, y1, z1, x2, y2, z2, id);
        setFacesMatcher(x1, y1, z1, x2, y2, z2, id);
    }

    public boolean matches(IBlockReader world, BlockPos cornerPos)
    {
        BlockPos.Mutable checkPos = cornerPos.toMutable();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                for (int k = 0; k < depth; k++)
                {
                    int id = pattern[i][j][k];

                    if (id >= 0 && !matchers[id].matches(world, checkPos.setAndOffset(cornerPos, i, j, k).toImmutable()))
                        return false;
                }
            }
        }

        return true;
    }

    public void addChildBlocks(MultiBlock multiBlock, BlockPos cornerPos)
    {
        BlockPos.Mutable checkPos = cornerPos.toMutable();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                for (int k = 0; k < depth; k++)
                {
                    if (pattern[i][j][k] >= -1)
                    {
                        checkPos.setAndOffset(cornerPos, i, j, k);

                        if (!(multiBlock.world().getTileEntity(checkPos) instanceof MultiChildTile))
                            MultiChildType.tryConvertBlock(multiBlock.world(), checkPos);

                        multiBlock.addChildBlock(checkPos.toImmutable());
                    }
                }
            }
        }
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    public int depth()
    {
        return depth;
    }

    //TODO: Remove
    public void print(PrintStream stream) {
        String s1 = "";

        for(int k = 0; k < this.pattern[0][0].length; ++k) {
            s1 = s1 + '-';
        }

        stream.println(s1);

        for(int j = 0; j < this.pattern[0].length; ++j) {
            stream.println("y:" + j);

            for(int i = 0; i < this.pattern.length; ++i) {
                String s2 = "";

                for(int k = 0; k < this.pattern[0][0].length; ++k) {
                    int d = this.pattern[i][j][k];
                    if (d < 0) {
                        s2 = s2 + '?';
                    } else {
                        s2 = s2 + d;
                    }
                }

                stream.println(s2);
            }
        }

        stream.println(s1);
    }
}
