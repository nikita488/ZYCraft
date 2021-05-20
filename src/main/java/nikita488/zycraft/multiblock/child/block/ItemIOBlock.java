package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIOBlock extends SidedInterfaceBlock
{
    public static final EnumProperty<ItemIOMode> IO_MODE = ZYBlockStateProperties.ITEM_IO_MODE;

    public ItemIOBlock(Properties properties)
    {
        super(properties);
        setDefaultState(getDefaultState().with(IO_MODE, ItemIOMode.ALL));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(new StringTextComponent("WIP").mergeStyle(TextFormatting.ITALIC, TextFormatting.YELLOW));

        if (!Screen.hasShiftDown() && !flag.isAdvanced())
        {
            tooltip.add(ZYLang.TOOLTIP_HINT);
        }
        else
        {
            tooltip.add(ZYLang.INTERFACE);
            tooltip.add(ZYLang.ITEM_IO_INFO);
            tooltip.add(ZYLang.ITEM_IO_FEATURE);
        }
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
