package nikita488.zycraft.item;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.util.BlockUtils;

import java.util.Collections;
import java.util.Set;

import net.minecraft.item.Item.Properties;

public class ScytheItem extends ToolItem
{
    private static final Set<Material> MATERIALS = Sets.newHashSet(Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_FIREPROOF_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.WEB, Material.BAMBOO_SAPLING, Material.BAMBOO, Material.LEAVES, Material.CORAL);

    public ScytheItem(Properties properties)
    {
        super(0.0F, 0.0F, ScytheItemTier.INSTANCE, Collections.emptySet(), properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        return MATERIALS.contains(state.getMaterial()) ? speed : 1.0F;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state)
    {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player)
    {
        if (!MATERIALS.contains(state.getMaterial()))
        {
            stack.hurtAndBreak(2, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            return true;
        }

        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

        if (stack.isEmpty())
            return true;

        PlayerInteractionManager manager = ((ServerPlayerEntity)player).gameMode;
        int range = 1;

        for (BlockPos destroyPos : BlockPos.betweenClosed(pos.getX() - range, pos.getY(), pos.getZ() - range, pos.getX() + range, pos.getY(), pos.getZ() + range))
        {
            if (destroyPos.equals(pos) || !world.mayInteract(manager.player, destroyPos))
                continue;

            BlockState destroyState = world.getBlockState(destroyPos);

            if (!MATERIALS.contains(destroyState.getMaterial()) || !BlockUtils.tryHarvestBlock(manager, destroyState, destroyPos.immutable()))
                continue;

            stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

            if (stack.isEmpty())
                break;
        }

        return true;
    }
}
