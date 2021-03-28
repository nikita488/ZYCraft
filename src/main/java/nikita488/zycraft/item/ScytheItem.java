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
        return state.matchesBlock(Blocks.COBWEB);
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

            if (!MATERIALS.contains(destroyState.getMaterial()) || !BlockUtils.tryHarvestBlock(manager, destroyState, destroyPos.toImmutable()))
                continue;

            stack.damageItem(1, player, entity -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));

            if (stack.isEmpty())
                break;
        }

        return true;
    }
}
