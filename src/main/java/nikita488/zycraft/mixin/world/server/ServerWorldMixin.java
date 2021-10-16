package nikita488.zycraft.mixin.world.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.UnloadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerWorldMixin
{
    @Inject(method = "unload", at = @At(value = "TAIL"))
    private void unload(LevelChunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new UnloadChunkEvent(chunk));
    }
}
