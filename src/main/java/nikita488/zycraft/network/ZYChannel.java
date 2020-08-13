package nikita488.zycraft.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.multiblock.network.RemoveMultiPacket;
import nikita488.zycraft.multiblock.network.UpdateMultiPacket;

public class ZYChannel
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("channel");
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(NAME)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> FMLNetworkConstants.NETVERSION)
            .simpleChannel();
    private static int id;

    public static void init()
    {
        INSTANCE.messageBuilder(UpdateContainerVarPacket.class, id++)
                .decoder(UpdateContainerVarPacket::decode)
                .encoder(UpdateContainerVarPacket::encode)
                .consumer(UpdateContainerVarPacket::handle)
                .add();
        INSTANCE.messageBuilder(AddMultiPacket.class, id++)
                .decoder(AddMultiPacket::decode)
                .encoder(AddMultiPacket::encode)
                .consumer(AddMultiPacket::handle)
                .add();
        INSTANCE.messageBuilder(RemoveMultiPacket.class, id++)
                .decoder(RemoveMultiPacket::decode)
                .encoder(RemoveMultiPacket::encode)
                .consumer(RemoveMultiPacket::handle)
                .add();
        INSTANCE.messageBuilder(UpdateMultiPacket.class, id++)
                .decoder(UpdateMultiPacket::decode)
                .encoder(UpdateMultiPacket::encode)
                .consumer(UpdateMultiPacket::handle)
                .add();
    }
}
