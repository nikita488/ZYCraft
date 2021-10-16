package nikita488.zycraft.multiblock.child.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;

import javax.annotation.Nullable;
import java.util.List;

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
    public BlockEntity createTileEntity(BlockState state, BlockGetter getter)
    {
        return ZYTiles.ITEM_IO.create();
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
