package nikita488.zycraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.LogicalSidedProvider;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;
import nikita488.zycraft.multiblock.MultiType;

import java.util.Optional;
import java.util.function.Supplier;

public class AddMultiPacket
{
    private final MultiType<?> type;
    private final ChunkPos mainChunk;
    private final int id;
    private MultiBlock multiBlock;
    private FriendlyByteBuf buffer;

    public AddMultiPacket(MultiBlock multiBlock)
    {
        this.type = multiBlock.type();
        this.mainChunk = multiBlock.mainChunk();
        this.id = multiBlock.id();
        this.multiBlock = multiBlock;
    }

    public AddMultiPacket(FriendlyByteBuf buffer)
    {
        this.type = buffer.readRegistryIdUnsafe(ZYRegistries.MULTI_TYPES.get());
        this.mainChunk = new ChunkPos(buffer.readVarInt(), buffer.readVarInt());
        this.id = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static AddMultiPacket decode(FriendlyByteBuf buffer)
    {
        return new AddMultiPacket(buffer);
    }

    public static void encode(AddMultiPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeRegistryIdUnsafe(ZYRegistries.MULTI_TYPES.get(), packet.type());
        buffer.writeVarInt(packet.mainChunk().x);
        buffer.writeVarInt(packet.mainChunk().z);
        buffer.writeVarInt(packet.id());
        packet.multiBlock.encode(buffer);
    }

    public static boolean handle(AddMultiPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            Optional<Level> world = LogicalSidedProvider.CLIENTWORLD.get(context.get().getDirection().getReceptionSide());
            MultiBlock multiBlock = world.map(clientWorld -> packet.type().create(clientWorld, packet.mainChunk())).orElse(null);

            if (multiBlock == null)
                return;

            multiBlock.setID(packet.id());
            multiBlock.decode(packet.buffer());
            multiBlock.initChildBlocks();
            MultiManager.getInstance().addMultiBlock(multiBlock, MultiBlock.AddingReason.LOADED);
        });

        return true;
    }

    public MultiType<?> type()
    {
        return type;
    }

    public ChunkPos mainChunk()
    {
        return mainChunk;
    }

    public int id()
    {
        return id;
    }

    public FriendlyByteBuf buffer()
    {
        return buffer;
    }
}
