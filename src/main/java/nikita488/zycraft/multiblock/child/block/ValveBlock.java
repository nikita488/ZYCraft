package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.block.state.properties.ValveIOMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;

import javax.annotation.Nullable;
import java.util.List;

public class ValveBlock extends MultiInterfaceBlock
{
    public static final EnumProperty<ValveIOMode> IO_MODE = ZYBlockStateProperties.VALVE_IO_MODE;

    public ValveBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(IO_MODE, ValveIOMode.IN));
    }

    @Override
    protected void addTooltip(List<ITextComponent> tooltip)
    {
        tooltip.add(ZYLang.VALVE_INFO);
        tooltip.add(ZYLang.VALVE_FEATURE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.VALVE.create();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        if (!player.isShiftKeyDown())
            return super.use(state, level, pos, player, hand, hitResult);

        level.setBlockAndUpdate(pos, state.cycle(IO_MODE));
        return ActionResultType.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(IO_MODE);
    }
}
