package nikita488.zycraft.multiblock.child.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import nikita488.zycraft.block.state.properties.InterfaceAxis;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYLang;
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
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(ZYLang.INTERFACE);

        if (!Screen.hasShiftDown() && !flag.isAdvanced())
            tooltip.add(ZYLang.TOOLTIP_HINT);
        else
            addTooltip(tooltip);
    }

    protected void addTooltip(List<Component> tooltip) {}

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof IMultiChild child)
        {
            if (child.hasParents() || player.isShiftKeyDown())
                return super.use(state, level, pos, player, hand, hitResult);

            //TODO: Fix multiblocks
            /*if (!level.isClientSide())
                MultiType.tryFormMultiBlock(state, level, pos, hitResult.getDirection());*/
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(AXIS);
    }
}
