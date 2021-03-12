package nikita488.zycraft.block;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.api.colorable.IColorChanger;
import nikita488.zycraft.enums.ZYDyeColor;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.tile.ColorableTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.block.AbstractBlock.Properties;

public class ColorableBlock extends Block
{
    public ColorableBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.COLORABLE.create();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        if (!Screen.hasShiftDown() && !flag.isAdvanced())
        {
            tooltip.add(ZYLang.TOOLTIP_HINT);
        }
        else
        {
            tooltip.add(ZYLang.COLORABLE);
            tooltip.add(ZYLang.COLORABLE_INFO);
            tooltip.add(ZYLang.COLORABLE_RGB);
            tooltip.add(ZYLang.COLORABLE_BRIGHTNESS);
            tooltip.add(ZYLang.COLORABLE_RESET);
            tooltip.add(ZYLang.COLORABLE_DYE);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty())
            return ActionResultType.PASS;

        Item item = stack.getItem();
        TileEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof ColorableTile))
            return ActionResultType.CONSUME;

        ColorableTile colorable = (ColorableTile)tile;
        ZYDyeColor dyeColor;

        if (item instanceof IColorChanger && ((IColorChanger)item).canChangeColor(state, world, pos, player, hand, hit, colorable.rgb()))
        {
            if (!world.isClientSide)
                colorable.setRGB(((IColorChanger)item).changeColor(state, world, pos, player, hand, hit, colorable.rgb()));
            return ActionResultType.SUCCESS;
        }
        else if ((dyeColor = ZYDyeColor.byDyeColor(stack)) != null && colorable.rgb() != dyeColor.rgb())
        {
            if (world.isClientSide)
                return ActionResultType.SUCCESS;

            colorable.setRGB(dyeColor.rgb());
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    public int getColor(BlockState state, ColorableTile colorable, int tintIndex)
    {
        return colorable.rgb();
    }
}
