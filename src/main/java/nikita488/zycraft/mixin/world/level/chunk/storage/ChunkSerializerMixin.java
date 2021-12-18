package nikita488.zycraft.mixin.world.level.chunk.storage;

import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin
{
/*    @Inject(method = "lambda$postLoadChunk$10", at = @At(value = "TAIL"))
    private static void postLoadChunk(ListTag entitiesTag, ServerLevel level, ListTag blockEntitiesTag, LevelChunk chunk, CallbackInfo callback)
    {
        //lambda$postLoadChunk$10(Lnet/minecraft/nbt/ListTag;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/ListTag;Lnet/minecraft/world/level/chunk/LevelChunk;)V
        MinecraftForge.EVENT_BUS.post(new PostLoadChunkEvent(chunk, new CompoundTag()));
    }*/
}
