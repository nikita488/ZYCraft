package nikita488.zycraft.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nikita488.zycraft.ZYCraft;

public class ZYChannel
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("main");
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(NAME)
            .networkProtocolVersion(() -> FMLNetworkConstants.NETVERSION)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    private static int id;

    public static void init()
    {
        INSTANCE.messageBuilder(UpdateContainerVariablePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateContainerVariablePacket::decode)
                .encoder(UpdateContainerVariablePacket::encode)
                .consumer(UpdateContainerVariablePacket::handle)
                .add();
        INSTANCE.messageBuilder(SetRecipePatternPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetRecipePatternPacket::decode)
                .encoder(SetRecipePatternPacket::encode)
                .consumer(SetRecipePatternPacket::handle)
                .add();
        INSTANCE.messageBuilder(SetRecipePatternSlotPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetRecipePatternSlotPacket::decode)
                .encoder(SetRecipePatternSlotPacket::encode)
                .consumer(SetRecipePatternSlotPacket::handle)
                .add();
    }
}
