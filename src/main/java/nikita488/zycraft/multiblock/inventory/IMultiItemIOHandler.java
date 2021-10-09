package nikita488.zycraft.multiblock.inventory;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nikita488.zycraft.block.state.properties.ItemIOMode;

public interface IMultiItemIOHandler extends IItemHandlerModifiable
{
    boolean isSupported(ItemIOMode mode);

    IItemHandler access(ItemIOMode mode);

    void setActive(ItemIOMode mode);

    void setInactive(ItemIOMode mode);

    boolean isActive(ItemIOMode mode);
}
