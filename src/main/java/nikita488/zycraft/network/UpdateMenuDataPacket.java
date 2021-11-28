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
    private final int menuID, index;
    private IMenuData variable;
    private PacketBuffer buffer;

    public UpdateMenuDataPacket(int menuID, int index, IMenuData variable)
    {
        this.menuID = menuID;
        this.index = index;
        this.variable = variable;
    }

    public UpdateMenuDataPacket(PacketBuffer buffer)
    {
        this.menuID = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateMenuDataPacket decode(PacketBuffer buffer)
    {
        return new UpdateMenuDataPacket(buffer);
    }

    public static void encode(UpdateMenuDataPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.menuID());
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

            Container menu = mc.player.containerMenu;

            if (menu.containerId == packet.menuID && menu instanceof ZYContainer)
                ((ZYContainer)menu).handleVariable(packet.id(), packet.buffer());
        });

        return true;
    }

    public int menuID()
    {
        return menuID;
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
