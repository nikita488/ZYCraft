package nikita488.zycraft.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.WorldEvent;

public class PlayerLoadedChunkEvent extends WorldEvent
{
    private final ServerPlayerEntity player;
    private final Chunk chunk;

    public PlayerLoadedChunkEvent(ServerPlayerEntity player, Chunk chunk)
    {
        super(player.getServerWorld());
        this.player = player;
        this.chunk = chunk;
    }

    public ServerPlayerEntity getPlayer()
    {
        return player;
    }

    public Chunk getChunk()
    {
        return chunk;
    }
}
