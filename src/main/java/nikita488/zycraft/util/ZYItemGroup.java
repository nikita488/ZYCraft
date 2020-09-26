package nikita488.zycraft.util;

import nikita488.zycraft.ZYCraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
    public ItemStack createIcon()
    {
        return iconFactory.get();
    }

    @Override
    public boolean hasSearchBar()
    {
        return super.hasSearchBar();//true;
    }

    @Override
    public ResourceLocation getBackgroundImage()
    {
        return super.getBackgroundImage();//ZYCraft.modLoc("textures/gui/container/creative_inventory/tab_zycraft.png");
    }

    @Override
    public ResourceLocation getTabsImage()
    {
        return super.getTabsImage();//ZYCraft.modLoc("textures/gui/container/creative_inventory/tabs.png");
    }

    @Override
    public int getLabelColor()
    {
        return super.getLabelColor();//0xAAAAAA;
    }

    @Override
    public int getSlotColor()
    {
        return super.getSlotColor();//0x80FFFFFF;
    }
}
