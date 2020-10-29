package nikita488.zycraft.util;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemStackUtils
{
    @Nonnull
    public static ItemStack copy(@Nonnull ItemStack stack, int size)
    {
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }
}
