package nikita488.zycraft.mixin.client.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.UnloadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
    @Inject(method = "unload", at = @At(value = "TAIL"))
    private void unload(Chunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new UnloadChunkEvent(chunk));
    }
}
