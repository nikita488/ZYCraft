package nikita488.zycraft.multiblock.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.MultiType;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.MultiClientCapabilities;
import nikita488.zycraft.network.IZYMessage;

import java.util.function.Supplier;

public class AddMultiPacket implements IZYMessage
{
    private final int id;
    private final MultiType type;
    private final ChunkPos parentChunk;
    private MultiBlock multiBlock;
    private PacketBuffer buffer;

    public AddMultiPacket(MultiBlock multiBlock)
    {
        this.id = multiBlock.id();
        this.type = multiBlock.type();
        this.parentChunk = multiBlock.mainChunk().pos();
        this.multiBlock = multiBlock;
    }

    public AddMultiPacket(PacketBuffer buffer)
    {
        this.id = buffer.readVarInt();
        this.type = buffer.readRegistryIdUnsafe(ZYRegistries.multiType());
        this.parentChunk = new ChunkPos(buffer.readVarInt(), buffer.readVarInt());
        this.buffer = buffer;
    }

    public static AddMultiPacket decode(PacketBuffer buf)
    {
        return new AddMultiPacket(buf);
    }

    public static void encode(AddMultiPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.id);
        buf.writeRegistryIdUnsafe(ZYRegistries.multiType(), msg.type);
        buf.writeVarInt(msg.parentChunk.x);
        buf.writeVarInt(msg.parentChunk.z);
        msg.multiBlock.encode(buf);
    }

    public static boolean handle(AddMultiPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> MultiClientCapabilities.getMultiWorld().ifPresent(world -> world.handleAddPacket(msg)));
        return true;
    }

    public int id()
    {
        return id;
    }

    public MultiType type()
    {
        return type;
    }

    public ChunkPos parentChunk()
    {
        return parentChunk;
    }

    public MultiBlock multiBlock()
    {
        return multiBlock;
    }

    public PacketBuffer buffer()
    {
        return buffer;
    }
}
