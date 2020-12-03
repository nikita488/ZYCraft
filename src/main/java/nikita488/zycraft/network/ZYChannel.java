package nikita488.zycraft.network;

import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.multiblock.network.RemoveMultiPacket;
import nikita488.zycraft.multiblock.network.UpdateMultiPacket;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ZYChannel
{
    public static final ResourceLocation NAME = ZYCraft.modLoc("main");
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(NAME)
            .networkProtocolVersion(() -> FMLNetworkConstants.NETVERSION)
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    public static final PacketDistributor<MultiBlock> TRACKING_MULTI_BLOCK = new PacketDistributor<>(ZYChannel::trackingMultiBlock, NetworkDirection.PLAY_TO_CLIENT);
    private static int id;

    private static Consumer<IPacket<?>> trackingMultiBlock(PacketDistributor<MultiBlock> distributor, Supplier<MultiBlock> multiSupplier)
    {
        return packet ->
        {
            MultiBlock multiBlock = multiSupplier.get();
            ((ServerChunkProvider)multiBlock.world().getChunkProvider()).chunkManager.getTrackingPlayers(multiBlock.mainChunk(), false)
                    .forEach(player -> player.connection.sendPacket(packet));
        };
    }

    public static void init()
    {
        INSTANCE.messageBuilder(UpdateContainerVarPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateContainerVarPacket::decode)
                .encoder(UpdateContainerVarPacket::encode)
                .consumer(UpdateContainerVarPacket::handle)
                .add();
        INSTANCE.messageBuilder(AddMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AddMultiPacket::decode)
                .encoder(AddMultiPacket::encode)
                .consumer(AddMultiPacket::handle)
                .add();
        INSTANCE.messageBuilder(RemoveMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RemoveMultiPacket::decode)
                .encoder(RemoveMultiPacket::encode)
                .consumer(RemoveMultiPacket::handle)
                .add();
        INSTANCE.messageBuilder(UpdateMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateMultiPacket::decode)
                .encoder(UpdateMultiPacket::encode)
                .consumer(UpdateMultiPacket::handle)
                .add();
    }
}
