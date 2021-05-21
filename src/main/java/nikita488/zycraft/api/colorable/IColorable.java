package nikita488.zycraft.api.colorable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.enums.ZYDyeColor;

public interface IColorable
{
    default int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintIndex)
    {
        return getColor(state, world, pos);
    }

    int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos);

    void setColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int rgb);

    static boolean isColorable(IBlockReader world, BlockPos pos)
    {
        return world.getTileEntity(pos) instanceof IColorable;
    }

    static ActionResultType interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.isEmpty())
            return ActionResultType.PASS;

        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof IColorable))
            return ActionResultType.CONSUME;

        Item item = stack.getItem();
        IColorable colorable = (IColorable)tile;
        int rgb = colorable.getColor(state, world, pos);

        if (item instanceof IColorChanger)
        {
            IColorChanger changer = (IColorChanger)item;

            if (!changer.canChangeColor(state, world, pos, player, hand, hit, rgb))
                return ActionResultType.PASS;

            if (!world.isRemote())
                colorable.setColor(state, world, pos, changer.changeColor(state, world, pos, player, hand, hit, rgb));
            return ActionResultType.SUCCESS;
        }

        ZYDyeColor dyeColor = ZYDyeColor.byDyeColor(stack);

        if (dyeColor == null || rgb == dyeColor.rgb())
            return ActionResultType.PASS;

        if (!world.isRemote())
            colorable.setColor(state, world, pos, dyeColor.rgb());
        return ActionResultType.SUCCESS;
    }
}
