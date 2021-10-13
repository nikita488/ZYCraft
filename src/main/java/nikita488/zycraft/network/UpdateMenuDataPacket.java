package nikita488.zycraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.menu.ZYContainer;
import nikita488.zycraft.menu.data.IMenuData;

import java.util.function.Supplier;

public class UpdateMenuDataPacket
{
    private final int windowID, index;
    private IMenuData variable;
    private PacketBuffer buffer;

    public UpdateMenuDataPacket(int windowID, int index, IMenuData variable)
    {
        this.windowID = windowID;
        this.index = index;
        this.variable = variable;
    }

    public UpdateMenuDataPacket(PacketBuffer buffer)
    {
        this.windowID = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateMenuDataPacket decode(PacketBuffer buf)
    {
        return new UpdateMenuDataPacket(buf);
    }

    public static void encode(UpdateMenuDataPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeVarInt(msg.id());
        msg.variable().update();
        msg.variable().encode(buf);
    }

    public static boolean handle(UpdateMenuDataPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Minecraft mc = LogicalSidedProvider.INSTANCE.get(ctx.get().getDirection().getReceptionSide());

            if (mc.player == null)
                return;

            Container container = mc.player.containerMenu;

            if (container.containerId == msg.windowID && container instanceof ZYContainer)
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

    public IMenuData variable()
    {
        return variable;
    }

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
