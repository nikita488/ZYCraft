package nikita488.zycraft.event;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.WorldEvent;

public class UnloadChunkEvent extends WorldEvent
{
    private final LevelChunk chunk;

    public UnloadChunkEvent(LevelChunk chunk)
    {
        super(chunk.getLevel());
        this.chunk = chunk;
    }

    public LevelChunk getChunk()
    {
        return chunk;
    }
}
