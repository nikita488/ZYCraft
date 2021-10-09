package nikita488.zycraft.menu.data;

import net.minecraft.network.PacketBuffer;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.multiblock.inventory.IMultiItemIOHandler;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Supplier;

public class IOMenuData implements IMenuData, Supplier<EnumSet<ItemIOMode>>
{
    private final EnumSet<ItemIOMode> activeModes = EnumSet.noneOf(ItemIOMode.class);
    @Nullable
    private Supplier<IMultiItemIOHandler> supplier;

    public IOMenuData() {}

    public IOMenuData(@Nullable Supplier<IMultiItemIOHandler> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean canBeUpdated()
    {
        if (supplier != null)
            for (ItemIOMode mode : ItemIOMode.VALUES)
                if (activeModes.contains(mode) != supplier.get().isActive(mode))
                    return true;
       return false;
    }

    @Override
    public void update()
    {
        activeModes.clear();

        if (supplier != null)
            for (ItemIOMode mode : ItemIOMode.VALUES)
                if (supplier.get().isActive(mode))
                    activeModes.add(mode);
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(activeModes.size());

        for (ItemIOMode mode : activeModes)
            buffer.writeEnumValue(mode);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        activeModes.clear();

        int activeCount = buffer.readVarInt();

        for (int i = 0; i < activeCount; i++)
            activeModes.add(buffer.readEnumValue(ItemIOMode.class));
    }

    @Override
    public EnumSet<ItemIOMode> get()
    {
        return activeModes;
    }
}
