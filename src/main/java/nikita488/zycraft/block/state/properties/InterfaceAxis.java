package nikita488.zycraft.block.state.properties;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public enum InterfaceAxis implements StringRepresentable
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
        return switch (axis)
        {
            case X -> X;
            case Y -> Y;
            case Z -> Z;
        };
    }

    public static void set(BlockState state, Level level, BlockPos pos, InterfaceAxis axis)
    {
        level.setBlock(pos, state.setValue(ZYBlockStateProperties.INTERFACE_AXIS, axis), Block.UPDATE_CLIENTS);
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
