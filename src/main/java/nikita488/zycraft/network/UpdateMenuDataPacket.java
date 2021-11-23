package nikita488.zycraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fmllegacy.LogicalSidedProvider;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import nikita488.zycraft.menu.ZYMenu;
import nikita488.zycraft.menu.data.IMenuData;

import java.util.function.Supplier;

public class UpdateMenuDataPacket
{
    private final int menuID, index;
    private IMenuData variable;
    private FriendlyByteBuf buffer;

    public UpdateMenuDataPacket(int menuID, int index, IMenuData variable)
    {
        this.menuID = menuID;
        this.index = index;
        this.variable = variable;
    }

    public UpdateMenuDataPacket(FriendlyByteBuf buffer)
    {
        this.menuID = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static UpdateMenuDataPacket decode(FriendlyByteBuf buffer)
    {
        return new UpdateMenuDataPacket(buffer);
    }

    public static void encode(UpdateMenuDataPacket packet, FriendlyByteBuf buffer)
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

            AbstractContainerMenu menu = mc.player.containerMenu;

            if (menu.containerId == packet.menuID && menu instanceof ZYMenu modMenu)
                modMenu.handleVariable(packet.id(), packet.buffer());
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

    public FriendlyByteBuf buffer()
    {
        return buffer;
    }
}
