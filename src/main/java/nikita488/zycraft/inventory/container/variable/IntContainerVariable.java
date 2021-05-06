package nikita488.zycraft.inventory.container.variable;

import net.minecraft.network.PacketBuffer;

import java.util.function.IntSupplier;

public class IntContainerVariable implements IContainerVariable
{
    private int value;
    private IntSupplier supplier;

    public IntContainerVariable() {}

    public IntContainerVariable(IntSupplier supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean needsUpdating()
    {
        return value != supplier.getAsInt();
    }

    @Override
    public void update()
    {
        this.value = supplier.getAsInt();
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(value);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.value = buffer.readVarInt();
    }

    public int value()
    {
        return value;
    }
}
