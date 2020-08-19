package nikita488.zycraft.multiblock.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiClientCapabilities;
import nikita488.zycraft.network.IZYMessage;

import java.util.function.Supplier;

public class UpdateMultiPacket implements IZYMessage
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
        ctx.get().enqueueWork(() -> MultiClientCapabilities.getMultiWorld().ifPresent(world -> world.handleUpdatePacket(msg)));
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
