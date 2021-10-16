package nikita488.zycraft.multiblock.child.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.block.state.properties.InterfaceAxis;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.MultiType;
import nikita488.zycraft.multiblock.child.IMultiChild;

import javax.annotation.Nullable;
import java.util.List;

public abstract class MultiInterfaceBlock extends MultiChildBlock
{
    public static final EnumProperty<InterfaceAxis> AXIS = ZYBlockStateProperties.INTERFACE_AXIS;

    public MultiInterfaceBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(AXIS, InterfaceAxis.ALL));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader getter, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(ZYLang.INTERFACE);

        if (!Screen.hasShiftDown() && !flag.isAdvanced())
            tooltip.add(ZYLang.TOOLTIP_HINT);
        else
            addTooltip(tooltip);
    }

    protected void addTooltip(List<ITextComponent> tooltip) {}

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        TileEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof IMultiChild)
        {
            if (((IMultiChild)blockEntity).hasParents() || player.isShiftKeyDown())
                return super.use(state, level, pos, player, hand, hitResult);

            if (!level.isClientSide())
                MultiType.tryFormMultiBlock(state, level, pos, hitResult.getDirection());
            return ActionResultType.sidedSuccess(level.isClientSide());
        }

        return ActionResultType.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(AXIS);
    }
}
