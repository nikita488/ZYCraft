package nikita488.zycraft.api.colorable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nikita488.zycraft.api.util.ZYDyeColor;

public interface IColorable
{
    default int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int tintIndex)
    {
        return getColor(state, getter, pos);
    }

    int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos);

    void setColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int rgb);

    static boolean isColorable(BlockGetter getter, BlockPos pos)
    {
        return getter.getBlockEntity(pos) instanceof IColorable;
    }

    static InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isEmpty())
            return InteractionResult.PASS;

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof IColorable)
        {
            IColorable colorable = (IColorable)blockEntity;
            int rgb = colorable.getColor(state, level, pos);

            if (stack.getItem() instanceof IColorChanger)
            {
                IColorChanger changer = (IColorChanger)stack.getItem();

                if (!changer.canChangeColor(state, level, pos, player, hand, hitResult, rgb))
                    return InteractionResult.PASS;

                if (!level.isClientSide())
                    colorable.setColor(state, level, pos, changer.changeColor(state, level, pos, player, hand, hitResult, rgb));
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            ZYDyeColor dyeColor = ZYDyeColor.byDyeColor(stack);

            if (dyeColor == null || rgb == dyeColor.rgb())
                return InteractionResult.PASS;

            if (!level.isClientSide())
                colorable.setColor(state, level, pos, dyeColor.rgb());
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.CONSUME;
    }
}
