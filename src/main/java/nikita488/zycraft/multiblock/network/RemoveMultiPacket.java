package nikita488.zycraft.multiblock.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiClientCapabilities;
import nikita488.zycraft.network.IZYMessage;

import java.util.function.Supplier;

public class RemoveMultiPacket implements IZYMessage
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
        ctx.get().enqueueWork(() -> MultiClientCapabilities.getMultiWorld().ifPresent(world -> world.handleRemovePacket(msg)));
        return true;
    }

    public int id()
    {
        return id;
    }
}
