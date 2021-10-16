package nikita488.zycraft.api.colorable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IColorChanger
{
    boolean canChangeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int color);

    int changeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int color);
}
