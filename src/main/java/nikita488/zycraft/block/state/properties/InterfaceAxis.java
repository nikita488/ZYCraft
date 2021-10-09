package nikita488.zycraft.block.state.properties;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public enum InterfaceAxis implements IStringSerializable
{
    ALL("all"),
    X("x"),
    Y("y"),
    Z("z");

    public static final InterfaceAxis[] VALUES = values();
    private final String name;

    InterfaceAxis(String name)
    {
        this.name = name;
    }

    public static InterfaceAxis get(Direction.Axis axis)
    {
        switch (axis)
        {
            case X:
                return X;
            case Y:
                return Y;
            case Z:
                return Z;
            default:
                return ALL;
        }
    }

    public static void set(BlockState state, World world, BlockPos pos, InterfaceAxis axis)
    {
        world.setBlockState(pos, state.with(ZYBlockStateProperties.INTERFACE_AXIS, axis), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public String getString()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
