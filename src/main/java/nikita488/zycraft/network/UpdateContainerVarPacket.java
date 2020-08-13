package nikita488.zycraft.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.container.BaseContainer;
import nikita488.zycraft.container.IContainerVar;

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
        buf.writeVarInt(msg.windowID);
        buf.writeVarInt(msg.index);
        msg.var.encode(buf);
        msg.var.update();
    }

    public static boolean handle(UpdateContainerVarPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            PlayerEntity player = Minecraft.getInstance().player;

            if (player.openContainer instanceof BaseContainer && player.openContainer.windowId == msg.windowID)
                ((BaseContainer)player.openContainer).handleUpdateVar(msg.index, msg.buffer);
        });

        return true;
    }
}
