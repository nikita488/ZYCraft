package nikita488.zycraft.mixin.world.chunk.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ChunkSerializer;
import net.minecraftforge.common.MinecraftForge;
import nikita488.zycraft.event.PostLoadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin
{
    @Inject(method = "readEntities", at = @At(value = "TAIL"))
    private static void readEntities(CompoundNBT tag, Chunk chunk, CallbackInfo callback)
    {
        MinecraftForge.EVENT_BUS.post(new PostLoadChunkEvent(chunk, tag));
    }
}
