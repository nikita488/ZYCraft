package nikita488.zycraft.item;

import nikita488.zycraft.enums.ZyType;
import net.minecraft.item.Item;

public class ZyItem extends Item
{
    protected final ZyType type;

    public ZyItem(ZyType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    public ZyType type()
    {
        return type;
    }
}
