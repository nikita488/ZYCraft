package nikita488.zycraft.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockUtils
{
    public static void sendBlockUpdated(World world, BlockPos pos, BlockState state)
    {
        sendBlockUpdated(world, pos, state, false);
    }

    public static void sendBlockUpdated(World world, BlockPos pos, BlockState state, boolean mainThread)
    {
        world.sendBlockUpdated(pos, state, state, mainThread ? Constants.BlockFlags.RERENDER_MAIN_THREAD : 0);
    }

    private static boolean removeBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean canHarvest)
    {
        boolean removed = state.removedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

        if (removed)
            state.getBlock().destroy(world, pos, state);

        return removed;
    }

    public static boolean tryHarvestBlock(PlayerEntity player,  BlockState state, BlockPos pos)
    {
        return !player.level.isClientSide && tryHarvestBlock(((ServerPlayerEntity)player).gameMode, state, pos);
    }

    public static boolean tryHarvestBlock(PlayerInteractionManager manager, BlockState state, BlockPos pos)
    {
        ServerWorld world = manager.level;
        ServerPlayerEntity player = manager.player;
        int experience = ForgeHooks.onBlockBreakEvent(world, manager.getGameModeForPlayer(), player, pos);

        if (experience == -1 || player.getMainHandItem().onBlockStartBreak(pos, player) || player.blockActionRestricted(world, pos, manager.getGameModeForPlayer()))
            return false;

        if (manager.isCreative())
        {
            removeBlock(state, world, pos, player, false);
            return true;
        }

        ItemStack heldStack = player.getMainHandItem();
        ItemStack heldStackCopy = heldStack.copy();
        boolean canHarvest = state.canHarvestBlock(world, pos, player);

        if (heldStack.isEmpty() && !heldStackCopy.isEmpty())
            ForgeEventFactory.onPlayerDestroyItem(player, heldStackCopy, Hand.MAIN_HAND);

        boolean removed = removeBlock(state, world, pos, player, canHarvest);

        if (removed && canHarvest)
            state.getBlock().playerDestroy(world, player, pos, state, state.hasTileEntity() ? world.getBlockEntity(pos) : null, heldStackCopy);

        if (removed && experience > 0)
            state.getBlock().popExperience(world, pos, experience);

        return true;
    }
}
