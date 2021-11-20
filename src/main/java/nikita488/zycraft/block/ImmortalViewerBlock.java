package nikita488.zycraft.block;

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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.init.ZYBlockEntities;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.util.ParticleUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ImmortalViewerBlock extends ViewerBlock implements EntityBlock
{
    public ImmortalViewerBlock(ViewerType type, Properties properties)
    {
        super(type, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ZYBlockEntities.COLORABLE.create(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(ZYLang.COLORABLE);

        if (!Screen.hasShiftDown() && !flag.isAdvanced())
        {
            tooltip.add(ZYLang.TOOLTIP_HINT);
        }
        else
        {
            tooltip.add(ZYLang.COLORABLE_INFO);
            tooltip.add(ZYLang.COLORABLE_RGB);
            tooltip.add(ZYLang.COLORABLE_BRIGHTNESS);
            tooltip.add(ZYLang.COLORABLE_RESET);
            tooltip.add(ZYLang.COLORABLE_DYE);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        return IColorable.interact(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
    {
        if (type == ViewerType.GLOWING)
            ParticleUtils.glowingColorableBlock(state, level, pos, random);
    }
}
