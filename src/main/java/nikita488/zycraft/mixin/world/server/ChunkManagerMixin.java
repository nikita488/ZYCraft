package nikita488.zycraft.mixin.world.server;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.PlayerLoadedChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkManagerMixin
{
    @Inject(method = "playerLoadedChunk", at = @At(value = "TAIL"))
    private void playerLoadedChunk(ServerPlayer player, Packet<?>[] packets, LevelChunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerLoadedChunkEvent(player, chunk));
    }
}
