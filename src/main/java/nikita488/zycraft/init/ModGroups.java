package nikita488.zycraft.init;

import nikita488.zycraft.enums.ZyType;
import net.minecraft.item.ItemGroup;

public class ModGroups
{
    public static final ItemGroup BLOCKS = new ZyItemGroup("blocks", () -> ModBlocks.ZYCHORIUM_BRICKS.get(ZyType.BLUE).asStack());
    public static final ItemGroup ITEMS = new ZyItemGroup("items", () -> ModItems.ZYCHORIUM.get(ZyType.BLUE).asStack());

    public static void init() {}
}
