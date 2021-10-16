package nikita488.zycraft.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.api.colorable.IColorChanger;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.util.ItemStackUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ColorScannerItem extends Item implements IColorChanger
{
    public ColorScannerItem(Properties properties)
    {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        int color = ItemStackUtils.getInt(stack, "Color", 0xFFFFFF);

        tooltip.add(ZYLang.COLOR_SCANNER_CURRENT_COLOR);
        tooltip.add(ZYLang.copy(ZYLang.COLOR_SCANNER_RED, (color >> 16) & 255));
        tooltip.add(ZYLang.copy(ZYLang.COLOR_SCANNER_GREEN, (color >> 8) & 255));
        tooltip.add(ZYLang.copy(ZYLang.COLOR_SCANNER_BLUE, color & 255));

        if (!Screen.hasShiftDown() && !flag.isAdvanced())
        {
            tooltip.add(ZYLang.TOOLTIP_HINT);
        }
        else
        {
            tooltip.add(ZYLang.COLOR_SCANNER_APPLY);
            tooltip.add(ZYLang.COLOR_SCANNER_COPY);
        }
    }

    @Override
    public boolean canChangeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int color)
    {
        return color != ItemStackUtils.getInt(player.getItemInHand(hand), "Color", 0xFFFFFF);
    }

    @Override
    public int changeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int color)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.isShiftKeyDown())
            return ItemStackUtils.getInt(stack, "Color", 0xFFFFFF);
        else
            stack.getOrCreateTag().putInt("Color", color);

        return color;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader reader, BlockPos pos, Player player)
    {
        return IColorable.isColorable(reader, pos);
    }
}
