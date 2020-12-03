package nikita488.zycraft.multiblock.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;
import nikita488.zycraft.multiblock.MultiType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AddMultiPacket
{
    private final MultiType<?> type;
    private final ChunkPos mainChunk;
    private final int id;
    private MultiBlock multiBlock;
    private PacketBuffer buffer;

    public AddMultiPacket(@Nonnull MultiBlock multiBlock)
    {
        this.type = multiBlock.getType();
        this.mainChunk = multiBlock.mainChunk();
        this.id = multiBlock.id();
        this.multiBlock = multiBlock;
    }

    public AddMultiPacket(@Nonnull PacketBuffer buffer)
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
        buf.writeRegistryIdUnsafe(ZYRegistries.MULTI_TYPES.get(), msg.type);
        buf.writeVarInt(msg.mainChunk.x);
        buf.writeVarInt(msg.mainChunk.z);
        buf.writeVarInt(msg.id);
        msg.multiBlock.encode(buf);
    }

    public static boolean handle(AddMultiPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            MultiBlock multiBlock = msg.getType().create(Minecraft.getInstance().world, msg.getMainChunk());

            if (multiBlock == null)
                return;

            multiBlock.setID(msg.getID());
            multiBlock.decode(msg.getBuffer());
            multiBlock.initChildBlocks();
            MultiManager.CLIENT_INSTANCE.addMultiBlock(multiBlock);
        });

        return true;
    }

    public MultiType<?> getType()
    {
        return type;
    }

    public ChunkPos getMainChunk()
    {
        return mainChunk;
    }

    public int getID()
    {
        return id;
    }

    public PacketBuffer getBuffer()
    {
        return buffer;
    }
}
