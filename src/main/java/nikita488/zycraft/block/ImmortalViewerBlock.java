package nikita488.zycraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.util.ParticleUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ImmortalViewerBlock extends ViewerBlock
{
    public ImmortalViewerBlock(ViewerType type, Properties properties)
    {
        super(type, properties);
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
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
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
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return IColorable.interact(state, world, pos, player, hand, hit);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (type == ViewerType.GLOWING)
            ParticleUtils.glowingColorableBlock(state, world, pos, rand);
    }
}
