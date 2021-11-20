package nikita488.zycraft.init;

import net.minecraft.world.item.CreativeModeTab;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.ZYCreativeModeTab;

public class ZYCreativeModeTabs
{
    public static final CreativeModeTab BLOCKS = new ZYCreativeModeTab("blocks", () -> ZYBlocks.ZYCHORIUM_BRICKS.get(ZYType.BLUE).asStack());
    public static final CreativeModeTab ITEMS = new ZYCreativeModeTab("items", () -> ZYItems.ZYCHORIUM.get(ZYType.BLUE).asStack());
    public static final CreativeModeTab FLUIDS = new ZYCreativeModeTab("fluids", ZYItems.QUARTZ_BUCKET::asStack);

    public static void init() {}
}
