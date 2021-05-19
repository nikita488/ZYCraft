package nikita488.zycraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetSlotStackPacket
{
    private final int windowID, slotIndex;
    private final ItemStack stack;

    public SetSlotStackPacket(int windowID, int slotIndex, ItemStack stack)
    {
        this.windowID = windowID;
        this.slotIndex = slotIndex;
        this.stack = stack;
    }

    public SetSlotStackPacket(PacketBuffer buf)
    {
        this.windowID = buf.readVarInt();
        this.slotIndex = buf.readVarInt();
        this.stack = buf.readItemStack();
    }

    public static SetSlotStackPacket decode(PacketBuffer buf)
    {
        return new SetSlotStackPacket(buf);
    }

    public static void encode(SetSlotStackPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeVarInt(msg.slotIndex());
        buf.writeItemStack(msg.stack());
    }

    public static boolean handle(SetSlotStackPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = ctx.get().getSender();

            if (player == null)
                return;

            player.markPlayerActive();

            Container container = player.openContainer;

            if (container.windowId == msg.windowID() && container.getCanCraft(player) && !player.isSpectator())
                container.getSlot(msg.slotIndex()).putStack(msg.stack());
        });

        return true;
    }

    public int windowID()
    {
        return windowID;
    }

    public int slotIndex()
    {
        return slotIndex;
    }

    public ItemStack stack()
    {
        return stack;
    }
}
