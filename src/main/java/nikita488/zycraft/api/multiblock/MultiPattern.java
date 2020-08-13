package nikita488.zycraft.api.multiblock;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.api.multiblock.child.IMultiChildMatcher;
import nikita488.zycraft.api.util.Cuboid6i;

public class MultiPattern
{
    private final byte[] pattern;
    private final ObjectArrayList<IMultiChildMatcher> matchers = new ObjectArrayList<>();
    private final int width, height, depth;
    private final Direction[] VALUES = Direction.values();

    public MultiPattern(int width, int height, int depth)
    {
        this.pattern = new byte[width * height * depth];
        this.width = width;
        this.height = height;
        this.depth = depth;
        setMatcher(null);
    }

    private int index(int x, int y, int z)
    {
        return x + width * (y + height * z);
    }

    private byte addMatcher(IMultiChildMatcher matcher)
    {
        if (matcher == null)
            return -1;

        byte index = (byte)matchers.indexOf(matcher);

        if (index == -1 && matchers.add(matcher))
            return (byte)(matchers.size() - 1);
        return index;
    }

    public void setMatcher(IMultiChildMatcher matcher)
    {
        setMatcher(0, 0, 0, width - 1, height - 1, depth - 1, matcher);
    }

    public void setMatcher(Cuboid6i cuboid, IMultiChildMatcher matcher)
    {
        setMatcher(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
    }

    public void setMatcher(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IMultiChildMatcher matcher)
    {
        byte index = addMatcher(matcher);
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    pattern[index(x, y, z)] = index;
    }

    public void setFaceMatcher(IMultiChildMatcher matcher)
    {
        setFaceMatcher(0, 0, 0, width - 1, height - 1, depth - 1, matcher);
    }

    public void setFaceMatcher(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IMultiChildMatcher matcher)
    {
        setFaceMatcher(new Cuboid6i(minX, minY, minZ, maxX, maxY, maxZ), matcher);
    }

    public void setFaceMatcher(Cuboid6i cuboid, IMultiChildMatcher matcher)
    {
        cuboid = cuboid.shrink(1);

        for (Direction side : VALUES)
            setMatcher(cuboid.expand(side).face(side), matcher);
    }

    public void setEdgeMatcher(IMultiChildMatcher matcher)
    {
        setEdgeMatcher(0, 0, 0, width - 1, height - 1, depth - 1, matcher);
    }

    public void setEdgeMatcher(Cuboid6i cuboid, IMultiChildMatcher matcher)
    {
        setEdgeMatcher(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
    }

    public void setEdgeMatcher(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IMultiChildMatcher matcher)
    {
        byte index = addMatcher(matcher);

        for (int x = minX + 1; x <= maxX - 1; x++)
        {
            pattern[index(x, minY, minZ)] = index;
            pattern[index(x, minY, maxZ)] = index;
            pattern[index(x, maxY, minZ)] = index;
            pattern[index(x, maxY, maxZ)] = index;
        }

        for (int y = minY; y <= maxY; y++)
        {
            pattern[index(minX, y, minZ)] = index;
            pattern[index(minX, y, maxZ)] = index;
            pattern[index(maxX, y, minZ)] = index;
            pattern[index(maxX, y, maxZ)] = index;
        }

        for (int z = minZ + 1; z <= maxZ - 1; z++)
        {
            pattern[index(minX, minY, z)] = index;
            pattern[index(minX, maxY, z)] = index;
            pattern[index(maxX, minY, z)] = index;
            pattern[index(maxX, maxY, z)] = index;
        }
    }

    public void setCornerMatcher(IMultiChildMatcher matcher)
    {
        setCornerMatcher(0, 0, 0, width - 1, height - 1, depth - 1, matcher);
    }

    public void setCornerMatcher(Cuboid6i cuboid, IMultiChildMatcher matcher)
    {
        setCornerMatcher(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ(), matcher);
    }

    public void setCornerMatcher(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IMultiChildMatcher matcher)
    {
        byte index = addMatcher(matcher);

        pattern[index(minX, minY, minZ)] = index;
        pattern[index(minX, minY, maxZ)] = index;
        pattern[index(minX, maxY, minZ)] = index;
        pattern[index(minX, maxY, maxZ)] = index;
        pattern[index(maxX, minY, minZ)] = index;
        pattern[index(maxX, minY, maxZ)] = index;
        pattern[index(maxX, maxY, minZ)] = index;
        pattern[index(maxX, maxY, maxZ)] = index;
    }

    public void setSurfaceMatcher(IMultiChildMatcher matcher)
    {
        setSurfaceMatcher(0, 0, 0, width - 1, height - 1, depth - 1, matcher);
    }

    public void setSurfaceMatcher(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IMultiChildMatcher matcher)
    {
        setSurfaceMatcher(new Cuboid6i(minX, minY, minZ, maxX, maxY, maxZ), matcher);
    }

    public void setSurfaceMatcher(Cuboid6i cuboid, IMultiChildMatcher matcher)
    {
        for (Direction side : VALUES)
            setMatcher(cuboid.face(side), matcher);
    }

    public boolean matches(IBlockReader world, BlockPos startPos)
    {
        BlockPos.Mutable checkPos = new BlockPos.Mutable();
        byte index;

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < depth; z++)
                    if ((index = pattern[index(x, y, z)]) >= 0 && !matchers.get(index).matches(world, checkPos.setPos(startPos).move(x, y, z)))
                        return false;
        return true;
    }

    public void addBlocks(MultiBlock multiBlock, BlockPos bottomLeftCorner)
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < depth; z++)
                    multiBlock.addChildBlock(bottomLeftCorner.add(x, y, z));
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
}
