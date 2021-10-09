package nikita488.zycraft.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.WorldEvent;

public class UnloadChunkEvent extends WorldEvent
{
    private final Chunk chunk;

    public UnloadChunkEvent(Chunk chunk)
    {
        super(chunk.getWorld());
        this.chunk = chunk;
    }

    public Chunk getChunk()
    {
        return chunk;
    }
}
