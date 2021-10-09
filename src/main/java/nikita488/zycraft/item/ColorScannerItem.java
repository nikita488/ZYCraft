package nikita488.zycraft.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
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
    public boolean canChangeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        return color != ItemStackUtils.getInt(player.getHeldItem(hand), "Color", 0xFFFFFF);
    }

    @Override
    public int changeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!player.isSneaking())
            return ItemStackUtils.getInt(stack, "Color", 0xFFFFFF);
        else
            stack.getOrCreateTag().putInt("Color", color);

        return color;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return IColorable.isColorable(world, pos);
    }
}
