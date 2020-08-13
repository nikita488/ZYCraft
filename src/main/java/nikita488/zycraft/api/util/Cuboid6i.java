package nikita488.zycraft.api.util;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class Cuboid6i
{
    private final int minX, minY, minZ, maxX, maxY, maxZ;

    public Cuboid6i(Vec3i vec)
    {
        this(vec, vec);
    }

    public Cuboid6i(Vec3i min, Vec3i max)
    {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public Cuboid6i(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public Cuboid6i expand(Direction dir)
    {
        return expand(dir, 1);
    }

    public Cuboid6i expand(Direction dir, int amount)
    {
        switch(dir.getAxisDirection())
        {
            case NEGATIVE:
                return new Cuboid6i(
                        minX + dir.getXOffset() * amount,
                        minY + dir.getYOffset() * amount,
                        minZ + dir.getZOffset() * amount,
                        maxX, maxY, maxZ);
            case POSITIVE:
                return new Cuboid6i(
                        minX, minY, minZ,
                        maxX + dir.getXOffset() * amount,
                        maxY + dir.getYOffset() * amount,
                        maxZ + dir.getZOffset() * amount);
        }

        return this;
    }

    public Cuboid6i expand(int amount)
    {
        return expand(amount, amount, amount);
    }

    public Cuboid6i expand(int x, int y, int z)
    {
        return new Cuboid6i(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z);
    }

    public Cuboid6i shrink(Direction dir)
    {
        return shrink(dir, 1);
    }

    public Cuboid6i shrink(Direction dir, int amount)
    {
        return expand(dir, -amount);
    }

    public Cuboid6i shrink(int amount)
    {
        return shrink(amount, amount, amount);
    }

    public Cuboid6i shrink(int x, int y, int z)
    {
        return expand(-x, -y, -z);
    }

    public Cuboid6i face(Direction side)
    {
        switch (side)
        {
            case DOWN:
                return new Cuboid6i(minX, minY, minZ, maxX, minY, maxZ);
            case UP:
                return new Cuboid6i(minX, maxY, minZ, maxX, maxY, maxZ);
            case NORTH:
                return new Cuboid6i(minX, minY, minZ, maxX, maxY, minZ);
            case SOUTH:
                return new Cuboid6i(minX, minY, maxZ, maxX, maxY, maxZ);
            case WEST:
                return new Cuboid6i(minX, minY, minZ, minX, maxY, maxZ);
            case EAST:
                return new Cuboid6i(maxX, minY, minZ, maxX, maxY, maxZ);
        }

        return this;
    }

    public int dimension(Direction.Axis axis)
    {
        switch (axis)
        {
            case X:
                return width();
            case Y:
                return height();
            case Z:
                return depth();
        }

        return -1;
    }

    public BlockPos center()
    {
        return new BlockPos((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
    }

    public static Cuboid6i load(CompoundNBT tag)
    {
        return new Cuboid6i(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"), tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
    }

    public CompoundNBT save()
    {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("MinX", minX);
        tag.putInt("MinY", minY);
        tag.putInt("MinZ", minZ);
        tag.putInt("MaxX", maxX);
        tag.putInt("MaxY", maxY);
        tag.putInt("MaxZ", maxZ);

        return tag;
    }

    public static Cuboid6i decode(PacketBuffer buffer)
    {
        return new Cuboid6i(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public void encode(PacketBuffer buf)
    {
        buf.writeVarInt(minX);
        buf.writeVarInt(minY);
        buf.writeVarInt(minZ);
        buf.writeVarInt(maxX);
        buf.writeVarInt(maxY);
        buf.writeVarInt(maxZ);
    }

    public int width()
    {
        return maxX - minX + 1;
    }

    public int height()
    {
        return maxY - minY + 1;
    }

    public int depth()
    {
        return maxZ - minZ + 1;
    }

    public AxisAlignedBB asAABB()
    {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public int minX()
    {
        return minX;
    }

    public int minY()
    {
        return minY;
    }

    public int minZ()
    {
        return minZ;
    }

    public int maxX()
    {
        return maxX;
    }

    public int maxY()
    {
        return maxY;
    }

    public int maxZ()
    {
        return maxZ;
    }

    public BlockPos min()
    {
        return new BlockPos(minX, minY, minZ);
    }

    public BlockPos max()
    {
        return new BlockPos(maxX, maxY, maxZ);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("minX", minX)
                .add("minY", minY)
                .add("minZ", minZ)
                .add("maxX", maxX)
                .add("maxY", maxY)
                .add("maxZ", maxZ)
                .toString();
    }
}
