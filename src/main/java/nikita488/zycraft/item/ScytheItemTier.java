package nikita488.zycraft.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import nikita488.zycraft.init.ZYBlocks;

public enum ScytheItemTier implements IItemTier
{
    INSTANCE;

    @Override
    public int getUses()
    {
        return ItemTier.STONE.getUses();
    }

    @Override
    public float getSpeed()
    {
        return 15.0F;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return ItemTier.STONE.getAttackDamageBonus();
    }

    @Override
    public int getLevel()
    {
        return ItemTier.STONE.getLevel();
    }

    @Override
    public int getEnchantmentValue()
    {
        return ItemTier.STONE.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return Ingredient.of(ZYBlocks.QUARTZ_CRYSTAL.get());
    }
}
