package nikita488.zycraft.mixin.world.level.chunk.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.PostLoadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin
{
    @Inject(method = "postLoadChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;", at = @At(value = "TAIL"), cancellable = true)
    private static void postLoadChunk(ServerLevel level, CompoundTag tag, CallbackInfoReturnable<LevelChunk.PostLoadProcessor> callback)
    {
        LevelChunk.PostLoadProcessor processor = callback.getReturnValue();

        if (processor == null)
            return;

        //TODO: Double check this post load crap
        callback.setReturnValue(chunk ->
        {
            processor.run(chunk);
            MinecraftForge.EVENT_BUS.post(new PostLoadChunkEvent(chunk, tag));
        });
    }
}
