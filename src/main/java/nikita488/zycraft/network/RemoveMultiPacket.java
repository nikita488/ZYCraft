package nikita488.zycraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;

import java.util.function.Supplier;

public class RemoveMultiPacket
{
    private final int id;

    public RemoveMultiPacket(MultiBlock multiBlock)
    {
        this.id = multiBlock.id();
    }

    public RemoveMultiPacket(PacketBuffer buffer)
    {
        this.id = buffer.readVarInt();
    }

    public static RemoveMultiPacket decode(PacketBuffer buffer)
    {
        return new RemoveMultiPacket(buffer);
    }

    public static void encode(RemoveMultiPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.id());
    }

    public static boolean handle(RemoveMultiPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            MultiBlock multiBlock = MultiManager.getInstance().getMultiBlock(packet.id());

            if (multiBlock != null)
                MultiManager.getInstance().removeMultiBlock(multiBlock, MultiBlock.RemovalReason.UNLOADED);
        });

        return true;
    }

    public int id()
    {
        return id;
    }
}
