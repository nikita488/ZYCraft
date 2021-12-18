package nikita488.zycraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

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

    public SetSlotStackPacket(FriendlyByteBuf buffer)
    {
        this.menuID = buffer.readVarInt();
        this.slotIndex = buffer.readVarInt();
        this.stack = buffer.readItem();
    }

    public static SetSlotStackPacket decode(FriendlyByteBuf buffer)
    {
        return new SetSlotStackPacket(buffer);
    }

    public static void encode(SetSlotStackPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(packet.menuID());
        buffer.writeVarInt(packet.slotIndex());
        buffer.writeItem(packet.stack());
    }

    public static boolean handle(SetSlotStackPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayer player = context.get().getSender();

            if (player == null)
                return;

            player.resetLastActionTime();

            AbstractContainerMenu menu = player.containerMenu;

            if (menu.containerId == packet.menuID() && !player.isSpectator())
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
