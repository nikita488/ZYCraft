package nikita488.zycraft.init;

import net.minecraft.item.ItemGroup;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.ZYItemGroup;

public class ZYGroups
{
    public static final ItemGroup BLOCKS = new ZYItemGroup("blocks", () -> ZYBlocks.ZYCHORIUM_BRICKS.get(ZYType.BLUE).asStack());
    public static final ItemGroup ITEMS = new ZYItemGroup("items", () -> ZYItems.ZYCHORIUM.get(ZYType.BLUE).asStack());

    public static void init() {}
}
