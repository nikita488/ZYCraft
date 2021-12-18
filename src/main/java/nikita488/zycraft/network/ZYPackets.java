package nikita488.zycraft.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiBlock;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ZYPackets
{
    public static final PacketDistributor<MultiBlock> TRACKING_MULTI_BLOCK = new PacketDistributor<>(ZYPackets::trackingMultiBlock, NetworkDirection.PLAY_TO_CLIENT);
    private static int id;

    private static Consumer<Packet<?>> trackingMultiBlock(PacketDistributor<MultiBlock> distributor, Supplier<MultiBlock> supplier)
    {
        return packet ->
        {
            MultiBlock multiBlock = supplier.get();
            ((ServerChunkCache)multiBlock.level().getChunkSource()).chunkMap.getPlayers(multiBlock.mainChunk(), false)
                    .forEach(player -> player.connection.send(packet));
        };
    }

    public static void init()
    {
        SimpleChannel channel = ZYCraft.CHANNEL;

        //Clientbound
        channel.messageBuilder(UpdateMenuDataPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateMenuDataPacket::decode)
                .encoder(UpdateMenuDataPacket::encode)
                .consumer(UpdateMenuDataPacket::handle)
                .add();
        channel.messageBuilder(AddMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AddMultiPacket::decode)
                .encoder(AddMultiPacket::encode)
                .consumer(AddMultiPacket::handle)
                .add();
        channel.messageBuilder(RemoveMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RemoveMultiPacket::decode)
                .encoder(RemoveMultiPacket::encode)
                .consumer(RemoveMultiPacket::handle)
                .add();
        channel.messageBuilder(UpdateMultiPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateMultiPacket::decode)
                .encoder(UpdateMultiPacket::encode)
                .consumer(UpdateMultiPacket::handle)
                .add();
        //Serverbound
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
