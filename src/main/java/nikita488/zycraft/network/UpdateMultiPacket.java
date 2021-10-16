package nikita488.zycraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;

import java.util.function.Supplier;

public class UpdateMultiPacket
{
    private final int id;
    private MultiBlock multiBlock;
    private PacketBuffer buffer;

    public UpdateMultiPacket(MultiBlock multiBlock)
    {
        this.id = multiBlock.id();
        this.multiBlock = multiBlock;
    }

    public UpdateMultiPacket(PacketBuffer buffer)
    {
        this.id = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateMultiPacket decode(PacketBuffer buffer)
    {
        return new UpdateMultiPacket(buffer);
    }

    public static void encode(UpdateMultiPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.id());
        packet.multiBlock.encodeUpdate(buffer);
    }

    public static boolean handle(UpdateMultiPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            MultiBlock multiBlock = MultiManager.getInstance().getMultiBlock(packet.id());

            if (multiBlock != null)
                multiBlock.decodeUpdate(packet.buffer());
        });
        return true;
    }

    public int id()
    {
        return id;
    }

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
