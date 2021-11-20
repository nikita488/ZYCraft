package nikita488.zycraft.multiblock.child.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYBlockEntities;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.entity.ItemIOBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

import static nikita488.zycraft.util.BlockEntityUtils.createTickerHelper;

public class ItemIOBlock extends MultiInterfaceBlock
{
    public static final EnumProperty<ItemIOMode> IO_MODE = ZYBlockStateProperties.ITEM_IO_MODE;

    public ItemIOBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(IO_MODE, ItemIOMode.ANY));
    }

    @Override
    protected void addTooltip(List<Component> tooltip)
    {
        tooltip.add(ZYLang.ITEM_IO_INFO);
        tooltip.add(ZYLang.ITEM_IO_FEATURE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ZYBlockEntities.ITEM_IO.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide() ? null : createTickerHelper(type, ZYBlockEntities.ITEM_IO.get(), ItemIOBlockEntity::serverTick);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(IO_MODE);
    }
}
