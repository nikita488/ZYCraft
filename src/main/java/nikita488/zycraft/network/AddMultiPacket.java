package nikita488.zycraft.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
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
    private PacketBuffer buffer;

    public AddMultiPacket(MultiBlock multiBlock)
    {
        this.type = multiBlock.type();
        this.mainChunk = multiBlock.mainChunk();
        this.id = multiBlock.id();
        this.multiBlock = multiBlock;
    }

    public AddMultiPacket(PacketBuffer buffer)
    {
        this.type = buffer.readRegistryIdUnsafe(ZYRegistries.MULTI_TYPES.get());
        this.mainChunk = new ChunkPos(buffer.readVarInt(), buffer.readVarInt());
        this.id = buffer.readVarInt();
        this.buffer = buffer;
    }

    public static AddMultiPacket decode(PacketBuffer buf)
    {
        return new AddMultiPacket(buf);
    }

    public static void encode(AddMultiPacket msg, PacketBuffer buf)
    {
        buf.writeRegistryIdUnsafe(ZYRegistries.MULTI_TYPES.get(), msg.type());
        buf.writeVarInt(msg.mainChunk().x);
        buf.writeVarInt(msg.mainChunk().z);
        buf.writeVarInt(msg.id());
        msg.multiBlock.encode(buf);
    }

    public static boolean handle(AddMultiPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
            MultiBlock multiBlock = world.map(clientWorld -> msg.type().create(clientWorld, msg.mainChunk())).orElse(null);

            if (multiBlock == null)
                return;

            multiBlock.setID(msg.id());
            multiBlock.decode(msg.buffer());
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

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
