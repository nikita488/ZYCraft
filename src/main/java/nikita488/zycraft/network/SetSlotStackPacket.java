package nikita488.zycraft.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetSlotStackPacket
{
    private final int menuID, slotIndex;
    private final ItemStack stack;

    public SetSlotStackPacket(int menuID, int slotIndex, ItemStack stack)
    {
        this.menuID = menuID;
        this.slotIndex = slotIndex;
        this.stack = stack;
    }

    public SetSlotStackPacket(PacketBuffer buffer)
    {
        this.menuID = buffer.readVarInt();
        this.slotIndex = buffer.readVarInt();
        this.stack = buffer.readItem();
    }

    public static SetSlotStackPacket decode(PacketBuffer buffer)
    {
        return new SetSlotStackPacket(buffer);
    }

    public static void encode(SetSlotStackPacket packet, PacketBuffer buffer)
    {
        buffer.writeVarInt(packet.menuID());
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

            Container menu = player.containerMenu;

            if (menu.containerId == packet.menuID() && menu.isSynched(player) && !player.isSpectator())
                menu.getSlot(packet.slotIndex()).set(packet.stack());
        });

        return true;
    }

    public int menuID()
    {
        return menuID;
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
