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
    default int getColor(BlockState state, IBlockDisplayReader getter, BlockPos pos, int tintIndex)
    {
        return getColor(state, getter, pos);
    }

    int getColor(BlockState state, IBlockDisplayReader getter, BlockPos pos);

    void setColor(BlockState state, IBlockDisplayReader getter, BlockPos pos, int rgb);

    static boolean isColorable(IBlockReader getter, BlockPos pos)
    {
        return getter.getBlockEntity(pos) instanceof IColorable;
    }

    static ActionResultType interact(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty())
            return ActionResultType.PASS;

        TileEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof IColorable)
        {
            IColorable colorable = (IColorable)blockEntity;
            int rgb = colorable.getColor(state, level, pos);

            if (stack.getItem() instanceof IColorChanger)
            {
                IColorChanger changer = (IColorChanger)stack.getItem();

                if (!changer.canChangeColor(state, level, pos, player, hand, hitResult, rgb))
                    return ActionResultType.PASS;

                if (!level.isClientSide())
                    colorable.setColor(state, level, pos, changer.changeColor(state, level, pos, player, hand, hitResult, rgb));
                return ActionResultType.sidedSuccess(level.isClientSide());
            }

            ZYDyeColor dyeColor = ZYDyeColor.byDyeColor(stack);

            if (dyeColor == null || rgb == dyeColor.rgb())
                return ActionResultType.PASS;

            if (!level.isClientSide())
                colorable.setColor(state, level, pos, dyeColor.rgb());
            return ActionResultType.sidedSuccess(level.isClientSide());
        }

        return ActionResultType.CONSUME;
    }
}
