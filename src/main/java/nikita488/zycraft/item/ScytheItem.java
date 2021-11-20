package nikita488.zycraft.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYLang;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ScytheItem extends Item
{
    private static final ObjectSet<Material> MATERIALS = Util.make(new ObjectOpenHashSet<>(), materials ->
            Collections.addAll(materials, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_FIREPROOF_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.WEB, Material.BAMBOO_SAPLING, Material.BAMBOO, Material.LEAVES));
    private boolean mining;

    public ScytheItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        float durability = (stack.getMaxDamage() - stack.getDamageValue()) / (float)stack.getMaxDamage();
        ChatFormatting formatting = ChatFormatting.RED;

        if (durability >= 0.6F)
            formatting = ChatFormatting.GREEN;
        else if (durability >= 0.25F)
            formatting = ChatFormatting.YELLOW;

        tooltip.add(ZYLang.copy(ZYLang.SCYTHE_DURABILITY, String.format("%.2f", durability * 100)).withStyle(formatting));
    }

    @Override
    public Component getName(ItemStack stack)
    {
        return new TranslatableComponent(getDescriptionId(stack)).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        return MATERIALS.contains(state.getMaterial()) ? 10F : 1F;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state)
    {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public int getEnchantmentValue()
    {
        return 1;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairItem)
    {
        return ZYBlocks.QUARTZ_CRYSTAL.isIn(repairItem);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity player)
    {
        stack.hurtAndBreak(2, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity player)
    {
        if (level.isClientSide() || mining)
            return false;

        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

        if (!stack.isEmpty() && !player.isShiftKeyDown() && player instanceof ServerPlayer serverPlayer)
        {
            int range = 2;

            this.mining = true;

            for (BlockPos destroyPos : BlockPos.betweenClosed(pos.getX() - range, pos.getY(), pos.getZ() - range, pos.getX() + range, pos.getY(), pos.getZ() + range))
            {
                if (destroyPos.equals(pos) || !level.mayInteract(serverPlayer, destroyPos) || !MATERIALS.contains(level.getBlockState(destroyPos).getMaterial()) || !serverPlayer.gameMode.destroyBlock(destroyPos.immutable()))
                    continue;

                stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));

                if (stack.isEmpty())
                    break;
            }

            this.mining = false;
        }

        return true;
    }
}
