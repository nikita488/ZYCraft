package nikita488.zycraft.multiblock.network;

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

    public static RemoveMultiPacket decode(PacketBuffer buf)
    {
        return new RemoveMultiPacket(buf);
    }

    public static void encode(RemoveMultiPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.id);
    }

    public static boolean handle(RemoveMultiPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            MultiBlock multiBlock = msg.getMultiBlock();

            if (multiBlock != null)
                MultiManager.CLIENT_INSTANCE.removeMultiBlock(multiBlock, true);
        });

        return true;
    }

    public MultiBlock getMultiBlock()
    {
        return MultiManager.CLIENT_INSTANCE.getMultiBlock(id);
    }
}
