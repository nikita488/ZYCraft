package nikita488.zycraft.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.world.WorldEvent;

public class PlayerLoadedChunkEvent extends WorldEvent
{
    private final ServerPlayer player;
    private final LevelChunk chunk;

    public PlayerLoadedChunkEvent(ServerPlayer player, LevelChunk chunk)
    {
        super(player.getLevel());
        this.player = player;
        this.chunk = chunk;
    }

    public ServerPlayer getPlayer()
    {
        return player;
    }

    public LevelChunk getChunk()
    {
        return chunk;
    }
}
