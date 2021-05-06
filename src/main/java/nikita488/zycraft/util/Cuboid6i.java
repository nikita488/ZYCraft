package nikita488.zycraft.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.stream.Stream;

@Immutable
public class Cuboid6i implements Iterable<BlockPos>
{
    private final int x1, y1, z1, x2, y2, z2;

    public Cuboid6i(Vector3i vec)
    {
        this(vec, vec);
    }

    public Cuboid6i(Vector3i vec1, Vector3i vec2)
    {
        this(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
    }

    public Cuboid6i(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
    }

    public Cuboid6i shrink(Direction side)
    {
        return shrink(side, 1);
    }

    public Cuboid6i shrink(Direction side, int amount)
    {
        return expand(side, -amount);
    }

    public Cuboid6i shrink(int amount)
    {
        return expand(-amount);
    }

    public Cuboid6i expand(Direction side)
    {
        return expand(side, 1);
    }

    public Cuboid6i expand(Direction side, int amount)
    {
        switch (side.getAxisDirection())
        {
            case NEGATIVE:
                return new Cuboid6i(x1 + side.getXOffset() * amount, y1 + side.getYOffset() * amount, z1 + side.getZOffset() * amount, x2, y2, z2);
            case POSITIVE:
                return new Cuboid6i(x1, y1, z1, x2 + side.getXOffset() * amount, y2 + side.getYOffset() * amount, z2 + side.getZOffset() * amount);
        }

        return this;
    }

    public Cuboid6i expand(int amount)
    {
        return new Cuboid6i(x1 - amount, y1 - amount, z1 - amount, x2 + amount, y2 + amount, z2 + amount);
    }

    public Cuboid6i side(Direction face)
    {
        Direction.Axis axis = face.getAxis();
        int side = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? min(axis) : max(axis);

        switch (axis)
        {
            case X:
                return new Cuboid6i(side, y1, z1, side, y2, z2);
            case Y:
                return new Cuboid6i(x1, side, z1, x2, side, z2);
            case Z:
                return new Cuboid6i(x1, y1, side, x2, y2, side);
        }

        return this;
    }

    public boolean contains(Vector3i vec)
    {
        return contains(vec.getX(), vec.getY(), vec.getZ());
    }

    public boolean contains(int x, int y, int z)
    {
        return x > x1 && y > y1 && z > z1 && x < x2 && y < y2 && z < z2;
    }

    public static Cuboid6i load(CompoundNBT tag)
    {
        return new Cuboid6i(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"), tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
    }

    public CompoundNBT save(CompoundNBT tag)
    {
        tag.putInt("MinX", x1);
        tag.putInt("MinY", y1);
        tag.putInt("MinZ", z1);
        tag.putInt("MaxX", x2);
        tag.putInt("MaxY", y2);
        tag.putInt("MaxZ", z2);

        return tag;
    }

    public static Cuboid6i decode(PacketBuffer buffer)
    {
        return new Cuboid6i(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(x1);
        buffer.writeVarInt(y1);
        buffer.writeVarInt(z1);
        buffer.writeVarInt(x2);
        buffer.writeVarInt(y2);
        buffer.writeVarInt(z2);
    }

    public int lengthX()
    {
        return x2 - x1 + 1;
    }

    public int lengthY()
    {
        return y2 - y1 + 1;
    }

    public int lengthZ()
    {
        return z2 - z1 + 1;
    }

    public int length(Direction.Axis axis)
    {
        return axis.getCoordinate(lengthX(), lengthY(), lengthZ());
    }

    public int minX()
    {
        return x1;
    }

    public int minY()
    {
        return y1;
    }

    public int minZ()
    {
        return z1;
    }

    public int maxX()
    {
        return x2;
    }

    public int maxY()
    {
        return y2;
    }

    public int maxZ()
    {
        return z2;
    }

    public int min(Direction.Axis axis)
    {
        return axis.getCoordinate(x1, y1, z1);
    }

    public int max(Direction.Axis axis)
    {
        return axis.getCoordinate(x2, y2, z2);
    }

    public BlockPos min()
    {
        return new BlockPos(x1, y1, z1);
    }

    public BlockPos max()
    {
        return new BlockPos(x2, y2, z2);
    }

    public BlockPos center()
    {
        return new BlockPos(x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2, z1 + (z2 - z1) / 2);
    }

    public int area()
    {
        return lengthX() * lengthY() * lengthZ();
    }

    @Override
    public Iterator<BlockPos> iterator()
    {
        return BlockPos.getAllInBoxMutable(x1, y1, z1, x2, y2, z2).iterator();
    }

    public Stream<BlockPos> stream()
    {
        return BlockPos.getAllInBox(x1, y1, z1, x2, y2, z2);
    }
}
