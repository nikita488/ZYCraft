package nikita488.zycraft.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import nikita488.zycraft.ZYCraft;

import java.util.function.Supplier;

public class ZYCreativeModeTab extends CreativeModeTab
{
    private final Supplier<ItemStack> iconFactory;

    public ZYCreativeModeTab(String label, Supplier<ItemStack> iconFactory)
    {
        super(ZYCraft.MOD_ID + "." + label);
        this.iconFactory = iconFactory;
    }

    @Override
    public ItemStack makeIcon()
    {
        return iconFactory.get();
    }
}
