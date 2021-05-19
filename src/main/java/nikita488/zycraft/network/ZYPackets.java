package nikita488.zycraft.network;

import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nikita488.zycraft.ZYCraft;

public class ZYPackets
{
    private static int id;

    public static void init()
    {
        SimpleChannel channel = ZYCraft.CHANNEL;

        channel.messageBuilder(UpdateContainerVariablePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateContainerVariablePacket::decode)
                .encoder(UpdateContainerVariablePacket::encode)
                .consumer(UpdateContainerVariablePacket::handle)
                .add();
        channel.messageBuilder(SetFabricatorRecipePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetFabricatorRecipePacket::decode)
                .encoder(SetFabricatorRecipePacket::encode)
                .consumer(SetFabricatorRecipePacket::handle)
                .add();
        channel.messageBuilder(SetSlotStackPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetSlotStackPacket::decode)
                .encoder(SetSlotStackPacket::encode)
                .consumer(SetSlotStackPacket::handle)
                .add();
    }
}
