package com.nikita488.zycraft.init;

import com.nikita488.zycraft.enums.ZyType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroups
{
    public static final ItemGroup BLOCKS = new ZyItemGroup("blocks", () -> new ItemStack(ModBlocks.ZYCHORIUM_BRICKS.get(ZyType.BLUE).get()));
    public static final ItemGroup ITEMS = new ZyItemGroup("items", () -> new ItemStack(ModItems.ZYCHORIUM.get(ZyType.BLUE).get()));

    public static void init() {}
}
