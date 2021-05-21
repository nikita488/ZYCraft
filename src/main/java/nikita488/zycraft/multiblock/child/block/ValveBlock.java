package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import nikita488.zycraft.block.state.properties.ValveIOMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;

import java.util.List;

public class ValveBlock extends SidedInterfaceBlock
{
    public static final EnumProperty<ValveIOMode> IO_MODE = ZYBlockStateProperties.VALVE_IO_MODE;

    public ValveBlock(Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState().with(IO_MODE, ValveIOMode.IN));
    }

    @Override
    protected void addTooltip(List<ITextComponent> tooltip)
    {
        tooltip.add(ZYLang.VALVE_INFO);
        tooltip.add(ZYLang.VALVE_FEATURE);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isRemote())
            world.setBlockState(pos, state.cycleValue(IO_MODE));
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(IO_MODE);
    }
}
