package nikita488.zycraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.container.IContainerVar;
import nikita488.zycraft.container.ZYContainer;

import java.util.function.Supplier;

public class UpdateContainerVarPacket
{
    private final int windowID, index;
    private IContainerVar var;
    private PacketBuffer buffer;

    public UpdateContainerVarPacket(int windowID, int index, IContainerVar var)
    {
        this.windowID = windowID;
        this.index = index;
        this.var = var;
    }

    public UpdateContainerVarPacket(PacketBuffer buffer)
    {
        this.windowID = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateContainerVarPacket decode(PacketBuffer buf)
    {
        return new UpdateContainerVarPacket(buf);
    }

    public static void encode(UpdateContainerVarPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeVarInt(msg.varIndex());
        msg.containerVar().update();
        msg.containerVar().encode(buf);
    }

    public static boolean handle(UpdateContainerVarPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Container container = Minecraft.getInstance().player.openContainer;

            if (container.windowId == msg.windowID && container instanceof ZYContainer)
                ((ZYContainer)container).handleContainerVar(msg.varIndex(), msg.buffer());
        });

        return true;
    }

    public int windowID()
    {
        return windowID;
    }

    public int varIndex()
    {
        return index;
    }

    public IContainerVar containerVar()
    {
        return var;
    }

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
