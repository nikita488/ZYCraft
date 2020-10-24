package nikita488.zycraft.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import nikita488.zycraft.init.ZYBlocks;

public enum ScytheItemTier implements IItemTier
{
    INSTANCE;

    @Override
    public int getMaxUses()
    {
        return ItemTier.STONE.getMaxUses();
    }

    @Override
    public float getEfficiency()
    {
        return 15.0F;
    }

    @Override
    public float getAttackDamage()
    {
        return ItemTier.STONE.getAttackDamage();
    }

    @Override
    public int getHarvestLevel()
    {
        return ItemTier.STONE.getHarvestLevel();
    }

    @Override
    public int getEnchantability()
    {
        return ItemTier.STONE.getEnchantability();
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(ZYBlocks.QUARTZ_CRYSTAL.get());
    }
}
