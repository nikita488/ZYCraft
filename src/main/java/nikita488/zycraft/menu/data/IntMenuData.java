package nikita488.zycraft.menu.data;

import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.util.function.IntSupplier;

public class IntMenuData implements IMenuData, IntSupplier
{
    private int value;
    @Nullable
    private IntSupplier supplier;

    public IntMenuData() {}

    public IntMenuData(@Nullable IntSupplier supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean canBeUpdated()
    {
        return supplier != null && value != supplier.getAsInt();
    }

    @Override
    public void update()
    {
        if (supplier != null)
            this.value = supplier.getAsInt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(value);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.value = buffer.readVarInt();
    }

    @Override
    public int getAsInt()
    {
        return value;
    }
}
