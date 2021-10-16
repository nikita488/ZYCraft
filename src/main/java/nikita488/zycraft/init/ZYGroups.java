package nikita488.zycraft.init;

import net.minecraft.world.item.CreativeModeTab;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.ZYItemGroup;

public class ZYGroups
{
    public static final CreativeModeTab BLOCKS = new ZYItemGroup("blocks", () -> ZYBlocks.ZYCHORIUM_BRICKS.get(ZYType.BLUE).asStack());
    public static final CreativeModeTab ITEMS = new ZYItemGroup("items", () -> ZYItems.ZYCHORIUM.get(ZYType.BLUE).asStack());
    public static final CreativeModeTab FLUIDS = new ZYItemGroup("fluids", ZYItems.QUARTZ_BUCKET::asStack);

    public static void init() {}
}
