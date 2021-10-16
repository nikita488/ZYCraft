package nikita488.zycraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;

import java.util.function.Supplier;

public class UpdateMultiPacket
{
    private final int id;
    private MultiBlock multiBlock;
    private FriendlyByteBuf buffer;

    public UpdateMultiPacket(MultiBlock multiBlock)
    {
        this.id = multiBlock.id();
        this.multiBlock = multiBlock;
    }

    public UpdateMultiPacket(FriendlyByteBuf buffer)
    {
        this.id = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateMultiPacket decode(FriendlyByteBuf buffer)
    {
        return new UpdateMultiPacket(buffer);
    }

    public static void encode(UpdateMultiPacket packet, FriendlyByteBuf buffer)
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

    public FriendlyByteBuf buffer()
    {
        return buffer;
    }
}
