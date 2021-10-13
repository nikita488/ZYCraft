package nikita488.zycraft.mixin.world.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkManager;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.PlayerLoadedChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkManager.class)
public class ChunkManagerMixin
{
    @Inject(method = "playerLoadedChunk", at = @At(value = "TAIL"))
    private void playerLoadedChunk(ServerPlayerEntity player, IPacket<?>[] packets, Chunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new PlayerLoadedChunkEvent(player, chunk));
    }
}
