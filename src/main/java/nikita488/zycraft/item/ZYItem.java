package nikita488.zycraft.item;

import nikita488.zycraft.enums.ZYType;
import net.minecraft.item.Item;

public class ZYItem extends Item
{
    protected final ZYType type;

    public ZYItem(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    public ZYType type()
    {
        return type;
    }
}
