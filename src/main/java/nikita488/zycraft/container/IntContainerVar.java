package nikita488.zycraft.container;

import net.minecraft.network.PacketBuffer;

import java.util.function.IntSupplier;

public class IntContainerVar implements IContainerVar
{
    private int value;
    private IntSupplier supplier;

    public IntContainerVar() {}

    public IntContainerVar(IntSupplier supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean isDirty()
    {
        return value != supplier.getAsInt();
    }

    @Override
    public void update()
    {
        value = supplier.getAsInt();
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(supplier.getAsInt());
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        value = buffer.readVarInt();
    }

    public int get()
    {
        return value;
    }
}
