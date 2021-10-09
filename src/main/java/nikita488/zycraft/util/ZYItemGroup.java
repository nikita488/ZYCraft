package nikita488.zycraft.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.ZYCraft;

import java.util.function.Supplier;

public class ZYItemGroup extends ItemGroup
{
    private final Supplier<ItemStack> iconFactory;

    public ZYItemGroup(String label, Supplier<ItemStack> iconFactory)
    {
        super(ZYCraft.MOD_ID + "." + label);
        this.iconFactory = iconFactory;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon()
    {
        return iconFactory.get();
    }
}
