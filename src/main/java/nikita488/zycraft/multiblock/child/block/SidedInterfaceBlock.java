package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import java.util.Map;

public abstract class SidedInterfaceBlock extends Block
{
    private static final Direction[] VALUES = Direction.values();
    public static final Map<Direction, BooleanProperty> SIDES = SixWayBlock.FACING_TO_PROPERTY_MAP;

    public SidedInterfaceBlock(Properties properties)
    {
        super(properties);
        SIDES.forEach((side, property) -> setDefaultState(getDefaultState().with(property, true)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        SIDES.values().forEach(builder::add);
    }
}
