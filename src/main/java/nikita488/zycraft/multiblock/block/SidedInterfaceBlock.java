package nikita488.zycraft.multiblock.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.multiblock.MultiChildType;
import nikita488.zycraft.multiblock.tile.MultiChildTile;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class SidedInterfaceBlock extends MultiInterfaceBlock
{
    private static final Direction[] VALUES = Direction.values();
    public static final Map<Direction, BooleanProperty> SIDES = SixWayBlock.FACING_TO_PROPERTY_MAP;

    public SidedInterfaceBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
/*        BlockState state = getDefaultState();
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        for (Direction side : VALUES)
        {
            BlockPos adjPos = pos.offset(side);
            state = state.with(SIDES.get(side), !world.getBlockState(adjPos).hasOpaqueCollisionShape(world, adjPos));
        }*/

        return getDefaultState();
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction side, BlockState adjState, IWorld world, BlockPos pos, BlockPos adjPos)
    {
        if (!adjState.hasTileEntity())
            return state;

        MultiChildTile child = getChild(world, adjPos);

        if (child == null || ZYBlocks.DEFAULT_MULTI_CHILD.get(MultiChildType.AIR).has(adjState))
            return state;

        return state.with(SIDES.get(side), false);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        SIDES.values().forEach(builder::add);
    }
}
