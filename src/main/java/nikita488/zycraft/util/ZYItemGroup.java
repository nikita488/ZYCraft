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
    public ItemStack makeIcon()
    {
        return iconFactory.get();
    }

    @Override
    public boolean hasSearchBar()
    {
        return super.hasSearchBar();//true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBackgroundImage()
    {
        return super.getBackgroundImage();//ZYCraft.modLoc("textures/gui/container/creative_inventory/tab_zycraft.png");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
