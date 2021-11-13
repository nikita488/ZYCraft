package nikita488.zycraft.util;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Immutable
public class Cuboid6i implements Iterable<BlockPos>
{
    public static final Codec<Cuboid6i> CODEC = Codec.INT_STREAM.comapFlatMap(stream -> Util.fixedSize(stream, 6)
            .map(bounds -> new Cuboid6i(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5])),
            cuboid -> IntStream.of(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ()))
            .stable();
    public static final Cuboid6i ZERO = new Cuboid6i(Vector3i.ZERO);
    protected int x1, y1, z1, x2, y2, z2;

    public Cuboid6i(Vector3i bounds)
    {
        this(bounds, bounds);
    }

    public Cuboid6i(Vector3i min, Vector3i max)
    {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public Cuboid6i(Cuboid6i cuboid)
    {
        this(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
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
        if (amount == 0)
            return this;

        switch (side.getAxisDirection())
        {
            case NEGATIVE:
                return new Cuboid6i(x1 + side.getStepX() * amount, y1 + side.getStepY() * amount, z1 + side.getStepZ() * amount, x2, y2, z2);
            case POSITIVE:
                return new Cuboid6i(x1, y1, z1, x2 + side.getStepX() * amount, y2 + side.getStepY() * amount, z2 + side.getStepZ() * amount);
        }

        return this;
    }

    public Cuboid6i expand(int amount)
    {
        return amount == 0 ? this : new Cuboid6i(x1 - amount, y1 - amount, z1 - amount, x2 + amount, y2 + amount, z2 + amount);
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

    public boolean contains(Vector3i pos)
    {
        return contains(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean contains(int x, int y, int z)
    {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
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

    public int width()
    {
        return x2 - x1 + 1;
    }

    public int height()
    {
        return y2 - y1 + 1;
    }

    public int depth()
    {
        return z2 - z1 + 1;
    }

    public int size(Direction.Axis axis)
    {
        return axis.choose(width(), height(), depth());
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

    protected void setMinX(int x)
    {
        this.x1 = x;
    }

    protected void setMinY(int y)
    {
        this.y1 = y;
    }

    protected void setMinZ(int z)
    {
        this.z1 = z;
    }

    protected void setMaxX(int x)
    {
        this.x2 = x;
    }

    protected void setMaxY(int y)
    {
        this.y2 = y;
    }

    protected void setMaxZ(int z)
    {
        this.z2 = z;
    }

    public int min(Direction.Axis axis)
    {
        return axis.choose(x1, y1, z1);
    }

    public int max(Direction.Axis axis)
    {
        return axis.choose(x2, y2, z2);
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

    public int volume()
    {
        return width() * height() * depth();
    }

    @Override
    public Iterator<BlockPos> iterator()
    {
        return BlockPos.betweenClosed(x1, y1, z1, x2, y2, z2).iterator();
    }

    public Stream<BlockPos> stream()
    {
        return BlockPos.betweenClosedStream(x1, y1, z1, x2, y2, z2);
    }

    public Cuboid6i immutable()
    {
        return this;
    }

    public Cuboid6i.Mutable mutable()
    {
        return new Cuboid6i.Mutable(this);
    }

    public Cuboid6i copy()
    {
        return new Cuboid6i(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof Cuboid6i)) return false;
        Cuboid6i cuboid = (Cuboid6i)obj;
        return x1 == cuboid.x1 && y1 == cuboid.y1 && z1 == cuboid.z1 && x2 == cuboid.x2 && y2 == cuboid.y2 && z2 == cuboid.z2;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x1", x1).add("y1", y1).add("z1", z1).add("x2", x2).add("y2", y2).add("z2", z2).toString();
    }

    public static class Mutable extends Cuboid6i
    {
        public Mutable()
        {
            super(ZERO);
        }

        public Mutable(Vector3i bounds)
        {
            super(bounds);
        }

        public Mutable(Vector3i min, Vector3i max)
        {
            super(min, max);
        }

        public Mutable(Cuboid6i cuboid)
        {
            super(cuboid);
        }

        public Mutable(int x1, int y1, int z1, int x2, int y2, int z2)
        {
            super(x1, y1, z1, x2, y2, z2);
        }

        public Mutable set(Vector3i bounds)
        {
            return set(bounds, bounds);
        }

        public Mutable set(Vector3i min, Vector3i max)
        {
            return set(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
        }

        public Mutable set(Cuboid6i cuboid)
        {
            return set(cuboid.minX(), cuboid.minY(), cuboid.minZ(), cuboid.maxX(), cuboid.maxY(), cuboid.maxZ());
        }

        public Mutable set(int x1, int y1, int z1, int x2, int y2, int z2)
        {
            setMinX(x1);
            setMinY(y1);
            setMinZ(z1);
            setMaxX(x2);
            setMaxY(y2);
            setMaxZ(z2);
            return this;
        }

        @Override
        public Mutable shrink(Direction side)
        {
            return super.shrink(side).mutable();
        }

        @Override
        public Mutable shrink(Direction side, int amount)
        {
            return super.shrink(side, amount).mutable();
        }

        @Override
        public Mutable shrink(int amount)
        {
            return super.shrink(amount).mutable();
        }

        @Override
        public Mutable expand(Direction side)
        {
            return super.expand(side).mutable();
        }

        @Override
        public Mutable expand(Direction side, int amount)
        {
            if (amount == 0)
                return this;

            switch (side.getAxisDirection())
            {
                case NEGATIVE:
                    return set(x1 + side.getStepX() * amount, y1 + side.getStepY() * amount, z1 + side.getStepZ() * amount, x2, y2, z2);
                case POSITIVE:
                    return set(x1, y1, z1, x2 + side.getStepX() * amount, y2 + side.getStepY() * amount, z2 + side.getStepZ() * amount);
            }

            return this;
        }

        @Override
        public Mutable expand(int amount)
        {
            return amount == 0 ? this : set(x1 - amount, y1 - amount, z1 - amount, x2 + amount, y2 + amount, z2 + amount);
        }

        @Override
        public Mutable side(Direction face)
        {
            Direction.Axis axis = face.getAxis();
            int side = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? min(axis) : max(axis);

            switch (axis)
            {
                case X:
                    return set(side, y1, z1, side, y2, z2);
                case Y:
                    return set(x1, side, z1, x2, side, z2);
                case Z:
                    return set(x1, y1, side, x2, y2, side);
            }

            return this;
        }

        @Override
        public void setMinX(int x)
        {
            super.setMinX(x);
        }

        @Override
        public void setMinY(int y)
        {
            super.setMinY(y);
        }

        @Override
        public void setMinZ(int z)
        {
            super.setMinZ(z);
        }

        @Override
        public void setMaxX(int x)
        {
            super.setMaxX(x);
        }

        @Override
        public void setMaxY(int y)
        {
            super.setMaxY(y);
        }

        @Override
        public void setMaxZ(int z)
        {
            super.setMaxZ(z);
        }

        @Override
        public Cuboid6i immutable()
        {
            return new Cuboid6i(this);
        }

        @Override
        public Mutable mutable()
        {
            return this;
        }

        @Override
        public Mutable copy()
        {
            return new Mutable(this);
        }
    }
}
