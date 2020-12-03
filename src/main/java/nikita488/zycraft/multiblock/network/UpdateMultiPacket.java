package nikita488.zycraft.multiblock.network;

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

    public static UpdateMultiPacket decode(PacketBuffer buf)
    {
        return new UpdateMultiPacket(buf);
    }

    public static void encode(UpdateMultiPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.id);
        msg.multiBlock.encodeUpdate(buf);
    }

    public static boolean handle(UpdateMultiPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            MultiBlock multiBlock = msg.getMultiBlock();

            if (multiBlock != null)
                multiBlock.decodeUpdate(msg.getBuffer());
        });
        return true;
    }

    public MultiBlock getMultiBlock()
    {
        return MultiManager.CLIENT_INSTANCE.getMultiBlock(id);
    }

    public PacketBuffer getBuffer()
    {
        return buffer;
    }
}
