package nikita488.zycraft.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Collections;
import java.util.Set;

public class ScytheItem extends ToolItem
{
    private static final Set<Material> MATERIALS = Sets.newHashSet(Material.PLANTS, Material.OCEAN_PLANT, Material.TALL_PLANTS, Material.NETHER_PLANTS, Material.SEA_GRASS, Material.WEB, Material.BAMBOO_SAPLING, Material.BAMBOO, Material.LEAVES, Material.CORAL);

    public ScytheItem(Properties properties)
    {
        super(0.0F, 0.0F, ScytheItemTier.INSTANCE, Collections.emptySet(), properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        return MATERIALS.contains(state.getMaterial()) ? efficiency : 1.0F;
    }

    @Override
    public boolean canHarvestBlock(BlockState state)
    {
        return state.isIn(Blocks.COBWEB);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player)
    {
        if (!MATERIALS.contains(state.getMaterial()))
        {
            stack.damageItem(2, player, entity -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            return true;
        }

        stack.damageItem(1, player, entity -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));

        if (stack.isEmpty())
            return true;

        PlayerInteractionManager manager = ((ServerPlayerEntity)player).interactionManager;
        int range = 1;

        for (BlockPos destroyPos : BlockPos.getAllInBoxMutable(pos.getX() - range, pos.getY(), pos.getZ() - range, pos.getX() + range, pos.getY(), pos.getZ() + range))
        {
            if (destroyPos.equals(pos) || !world.isBlockModifiable(manager.player, destroyPos))
                continue;

            BlockState destroyState = world.getBlockState(destroyPos);

            if (!MATERIALS.contains(destroyState.getMaterial()) || !tryHarvestBlock(manager, destroyState, destroyPos.toImmutable()))
                continue;

            stack.damageItem(1, player, entity -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));

            if (stack.isEmpty())
                break;
        }

        return true;
    }

    private boolean tryHarvestBlock(PlayerInteractionManager manager, BlockState state, BlockPos pos)
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
            state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), heldStackCopy);

        if (removed && experience > 0)
            state.getBlock().dropXpOnBlockBreak(world, pos, experience);

        return true;
    }

    private boolean removeBlock(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean canHarvest)
    {
        boolean removed = state.removedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

        if (removed)
            state.getBlock().onPlayerDestroy(world, pos, state);

        return removed;
    }
}
