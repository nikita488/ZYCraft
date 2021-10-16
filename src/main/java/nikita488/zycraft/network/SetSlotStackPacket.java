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

    public SetSlotStackPacket(PacketBuffer buffer)
    {
        this.windowID = buffer.readVarInt();
        this.slotIndex = buffer.readVarInt();
        this.stack = buffer.readItem();
    }

    public static SetSlotStackPacket decode(PacketBuffer buffer)
    {
        return new SetSlotStackPacket(buffer);
    }

    public static void encode(SetSlotStackPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.windowID());
        buffer.writeVarInt(packet.slotIndex());
        buffer.writeItem(packet.stack());
    }

    public static boolean handle(SetSlotStackPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = context.get().getSender();

            if (player == null)
                return;

            player.resetLastActionTime();

            Container container = player.containerMenu;

            if (container.containerId == packet.windowID() && container.isSynched(player) && !player.isSpectator())
                container.getSlot(packet.slotIndex()).set(packet.stack());
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
