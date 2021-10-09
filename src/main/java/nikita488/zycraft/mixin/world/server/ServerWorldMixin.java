package nikita488.zycraft.mixin.world.server;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.UnloadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
    @Inject(method = "onChunkUnloading", at = @At(value = "TAIL"))
    private void onChunkUnloading(Chunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new UnloadChunkEvent(chunk));
    }
}
