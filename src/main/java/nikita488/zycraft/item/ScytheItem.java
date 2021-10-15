package nikita488.zycraft.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYLang;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ScytheItem extends Item
{
    private static final ObjectSet<Material> MATERIALS = Util.make(new ObjectOpenHashSet<>(), materials ->
            Collections.addAll(materials, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT, Material.REPLACEABLE_FIREPROOF_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.WEB, Material.BAMBOO_SAPLING, Material.BAMBOO, Material.LEAVES, Material.CORAL));
    private boolean mining;

    public ScytheItem(Properties properties)
    {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        float durability = (stack.getMaxDamage() - stack.getDamageValue()) / (float)stack.getMaxDamage();
        TextFormatting formatting = TextFormatting.RED;

        if (durability >= 0.6F)
            formatting = TextFormatting.GREEN;
        else if (durability >= 0.25F)
            formatting = TextFormatting.YELLOW;

        tooltip.add(ZYLang.copy(ZYLang.SCYTHE_DURABILITY, String.format("%.2f", durability * 100)).withStyle(formatting));
    }

    @Override
    public ITextComponent getName(ItemStack stack)
    {
        return new TranslationTextComponent(getDescriptionId(stack)).withStyle(TextFormatting.BLUE);
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
        stack.hurtAndBreak(2, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity player)
    {
        if (world.isClientSide() || mining)
            return false;

        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

        if (!stack.isEmpty() && !player.isShiftKeyDown() && player instanceof ServerPlayerEntity)
        {
            PlayerInteractionManager gameMode = ((ServerPlayerEntity)player).gameMode;
            int range = 2;

            this.mining = true;

            for (BlockPos destroyPos : BlockPos.betweenClosed(pos.getX() - range, pos.getY(), pos.getZ() - range, pos.getX() + range, pos.getY(), pos.getZ() + range))
            {
                if (destroyPos.equals(pos) || !world.mayInteract(gameMode.player, destroyPos) || !MATERIALS.contains(world.getBlockState(destroyPos).getMaterial()) || !gameMode.destroyBlock(destroyPos.immutable()))
                    continue;

                stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));

                if (stack.isEmpty())
                    break;
            }

            this.mining = false;
        }

        return true;
    }
}
