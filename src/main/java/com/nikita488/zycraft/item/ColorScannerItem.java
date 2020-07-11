package com.nikita488.zycraft.item;

import com.nikita488.zycraft.block.ColorableBlock;
import com.nikita488.zycraft.tile.ColorableTile;
import com.nikita488.zycraft.util.Color4b;
import com.nikita488.zycraft.util.IColorChanger;
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
        CompoundNBT compound = stack.getTag();
        if (compound == null)
            return;

        int color = compound.getInt("Color");
        tooltip.add(new StringTextComponent("R: " + Color4b.component(color, 1)).applyTextStyle(TextFormatting.RED));
        tooltip.add(new StringTextComponent("G: " + Color4b.component(color, 2)).applyTextStyle(TextFormatting.GREEN));
        tooltip.add(new StringTextComponent("B: " + Color4b.component(color, 3)).applyTextStyle(TextFormatting.BLUE));
    }

    @Override
    public boolean canChangeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        CompoundNBT compound = player.getHeldItem(hand).getOrCreateTag();
        if (!compound.contains("Color", Constants.NBT.TAG_INT))
            compound.putInt("Color", 0xFFFFFF);
        return colorable.color().rgb() != compound.getInt("Color");
    }

    @Override
    public void changeColor(ColorableTile colorable, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        CompoundNBT compound = player.getHeldItem(hand).getTag();
        if (compound == null) return;
        if (!player.isCrouching())
            colorable.setRGB(compound.getInt("Color"));
        else
            colorable.color().saveRGB(compound);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return world.getBlockState(pos).getBlock() instanceof ColorableBlock;
    }
}
