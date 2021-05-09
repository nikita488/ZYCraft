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
        channel.messageBuilder(SetRecipePatternPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetRecipePatternPacket::decode)
                .encoder(SetRecipePatternPacket::encode)
                .consumer(SetRecipePatternPacket::handle)
                .add();
        channel.messageBuilder(SetRecipePatternSlotPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetRecipePatternSlotPacket::decode)
                .encoder(SetRecipePatternSlotPacket::encode)
                .consumer(SetRecipePatternSlotPacket::handle)
                .add();
    }
}
