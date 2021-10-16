package nikita488.zycraft.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.WorldEvent;

public class PostLoadChunkEvent extends WorldEvent
{
    private final LevelChunk chunk;
    private final CompoundTag tag;

    public PostLoadChunkEvent(LevelChunk chunk, CompoundTag tag)
    {
        super(chunk.getLevel());
        this.chunk = chunk;
        this.tag = tag;
    }

    public LevelChunk getChunk()
    {
        return chunk;
    }

    public CompoundTag getChunkTag()
    {
        return tag;
    }
}
