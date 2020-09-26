package nikita488.zycraft.item;

import net.minecraft.client.gui.screen.Screen;
import nikita488.zycraft.api.colorable.IColorChanger;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.init.ZYTextComponents;
import nikita488.zycraft.util.Color4b;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ColorScannerItem extends Item implements IColorChanger
{
    public ColorScannerItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        if (!Screen.hasShiftDown() && !flag.isAdvanced())
        {
            tooltip.add(ZYTextComponents.TOOLTIP_HINT);
        }
        else
        {
            tooltip.add(ZYTextComponents.COLOR_SCANNER_APPLY);
            tooltip.add(ZYTextComponents.COLOR_SCANNER_COPY);
        }

        int color = stack.hasTag() ? stack.getTag().getInt("Color") : 0xFFFFFF;

        tooltip.add(ZYTextComponents.COLOR_SCANNER_CURRENT_COLOR);
        tooltip.add(ZYTextComponents.getColorScannerRed((color >> 16) & 0xFF));
        tooltip.add(ZYTextComponents.getColorScannerGreen((color >> 8) & 0xFF));
        tooltip.add(ZYTextComponents.getColorScannerBlue(color & 0xFF));
    }

    @Override
    public boolean canChangeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        ItemStack stack = player.getHeldItem(hand);
        int scannerColor = stack.hasTag() ? stack.getTag().getInt("Color") : 0xFFFFFF;

        return color != scannerColor;
    }

    @Override
    public int changeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (!player.isCrouching())
            return stack.hasTag() ? stack.getTag().getInt("Color") : 0xFFFFFF;
        else
            stack.getOrCreateTag().putInt("Color", color);

        return color;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return world.getBlockState(pos).getBlock() instanceof ColorableBlock;
    }
}
