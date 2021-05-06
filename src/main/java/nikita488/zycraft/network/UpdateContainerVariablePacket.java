package nikita488.zycraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.inventory.container.variable.IContainerVariable;
import nikita488.zycraft.inventory.container.ZYContainer;

import java.util.function.Supplier;

public class UpdateContainerVariablePacket
{
    private final int windowID, index;
    private IContainerVariable variable;
    private PacketBuffer buffer;

    public UpdateContainerVariablePacket(int windowID, int index, IContainerVariable variable)
    {
        this.windowID = windowID;
        this.index = index;
        this.variable = variable;
    }

    public UpdateContainerVariablePacket(PacketBuffer buffer)
    {
        this.windowID = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateContainerVariablePacket decode(PacketBuffer buf)
    {
        return new UpdateContainerVariablePacket(buf);
    }

    public static void encode(UpdateContainerVariablePacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeVarInt(msg.id());
        msg.variable().update();
        msg.variable().encode(buf);
    }

    public static boolean handle(UpdateContainerVariablePacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Container container = Minecraft.getInstance().player.openContainer;

            if (container.windowId == msg.windowID && container instanceof ZYContainer)
                ((ZYContainer)container).handleVariable(msg.id(), msg.buffer());
        });

        return true;
    }

    public int windowID()
    {
        return windowID;
    }

    public int id()
    {
        return index;
    }

    public IContainerVariable variable()
    {
        return variable;
    }

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
