package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
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
    protected void addTooltip(List<ITextComponent> tooltip)
    {
        tooltip.add(ZYLang.ITEM_IO_INFO);
        tooltip.add(ZYLang.ITEM_IO_FEATURE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.ITEM_IO.create();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(IO_MODE);
    }
}
