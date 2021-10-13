package nikita488.zycraft.api.colorable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.api.util.ZYDyeColor;

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
        return world.getBlockEntity(pos) instanceof IColorable;
    }

    static ActionResultType interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty())
            return ActionResultType.PASS;

        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof IColorable)
        {
            IColorable colorable = (IColorable)tile;
            int rgb = colorable.getColor(state, world, pos);

            if (stack.getItem() instanceof IColorChanger)
            {
                IColorChanger changer = (IColorChanger)stack.getItem();

                if (!changer.canChangeColor(state, world, pos, player, hand, hit, rgb))
                    return ActionResultType.PASS;

                if (!world.isClientSide())
                    colorable.setColor(state, world, pos, changer.changeColor(state, world, pos, player, hand, hit, rgb));
                return ActionResultType.sidedSuccess(world.isClientSide());
            }

            ZYDyeColor dyeColor = ZYDyeColor.byDyeColor(stack);

            if (dyeColor == null || rgb == dyeColor.rgb())
                return ActionResultType.PASS;

            if (!world.isClientSide())
                colorable.setColor(state, world, pos, dyeColor.rgb());
            return ActionResultType.sidedSuccess(world.isClientSide());
        }

        return ActionResultType.CONSUME;
    }
}
