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
    public static void scheduleTileUpdate(World world, BlockPos pos, BlockState state)
    {
        if (world.isRemote) return;
        world.notifyBlockUpdate(pos, state, state, 0);
    }

    public static void scheduleRenderUpdate(World world, BlockPos pos)
    {
        scheduleRenderUpdate(world, pos, false);
    }

    public static void scheduleRenderUpdate(World world, BlockPos pos, boolean mainThread)
    {
        if (!world.isRemote) return;
        BlockState state = Blocks.AIR.getDefaultState();
        world.notifyBlockUpdate(pos, state, state, mainThread ? Constants.BlockFlags.RERENDER_MAIN_THREAD : 0);
    }

    private static boolean removeBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean canHarvest)
    {
        boolean removed = state.removedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

        if (removed)
            state.getBlock().onPlayerDestroy(world, pos, state);

        return removed;
    }

    public static boolean tryHarvestBlock(PlayerEntity player,  BlockState state, BlockPos pos)
    {
        return !player.world.isRemote && tryHarvestBlock(((ServerPlayerEntity)player).interactionManager, state, pos);
    }

    public static boolean tryHarvestBlock(PlayerInteractionManager manager, BlockState state, BlockPos pos)
    {
        ServerWorld world = manager.world;
        ServerPlayerEntity player = manager.player;
        int experience = ForgeHooks.onBlockBreakEvent(world, manager.getGameType(), player, pos);

        if (experience == -1 || player.getHeldItemMainhand().onBlockStartBreak(pos, player) || player.blockActionRestricted(world, pos, manager.getGameType()))
            return false;

        if (manager.isCreative())
        {
            removeBlock(state, world, pos, player, false);
            return true;
        }

        ItemStack heldStack = player.getHeldItemMainhand();
        ItemStack heldStackCopy = heldStack.copy();
        boolean canHarvest = state.canHarvestBlock(world, pos, player);

        if (heldStack.isEmpty() && !heldStackCopy.isEmpty())
            ForgeEventFactory.onPlayerDestroyItem(player, heldStackCopy, Hand.MAIN_HAND);

        boolean removed = removeBlock(state, world, pos, player, canHarvest);

        if (removed && canHarvest)
            state.getBlock().harvestBlock(world, player, pos, state, state.hasTileEntity() ? world.getTileEntity(pos) : null, heldStackCopy);

        if (removed && experience > 0)
            state.getBlock().dropXpOnBlockBreak(world, pos, experience);

        return true;
    }
}
