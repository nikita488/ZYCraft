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

    public static UpdateMenuDataPacket decode(PacketBuffer buffer)
    {
        return new UpdateMenuDataPacket(buffer);
    }

    public static void encode(UpdateMenuDataPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.windowID());
        buffer.writeVarInt(packet.id());
        packet.variable().update();
        packet.variable().encode(buffer);
    }

    public static boolean handle(UpdateMenuDataPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            Minecraft mc = LogicalSidedProvider.INSTANCE.get(context.get().getDirection().getReceptionSide());

            if (mc.player == null)
                return;

            Container container = mc.player.containerMenu;

            if (container.containerId == packet.windowID && container instanceof ZYContainer)
                ((ZYContainer)container).handleVariable(packet.id(), packet.buffer());
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
