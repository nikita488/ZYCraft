package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.init.ZYLang;

import javax.annotation.Nullable;
import java.util.List;
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
            addTooltip(tooltip);
        }
    }

    protected void addTooltip(List<ITextComponent> tooltip) {}

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        SIDES.values().forEach(builder::add);
    }
}
