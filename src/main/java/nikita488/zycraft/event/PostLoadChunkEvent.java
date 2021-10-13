package nikita488.zycraft.event;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.WorldEvent;

public class PostLoadChunkEvent extends WorldEvent
{
    private final Chunk chunk;
    private final CompoundNBT tag;

    public PostLoadChunkEvent(Chunk chunk, CompoundNBT tag)
    {
        super(chunk.getLevel());
        this.chunk = chunk;
        this.tag = tag;
    }

    public Chunk getChunk()
    {
        return chunk;
    }

    public CompoundNBT getChunkTag()
    {
        return tag;
    }
}
