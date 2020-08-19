package nikita488.zycraft.api.multiblock.capability;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.network.IZYMessage;

public interface IMultiChunk
{
    void addMultiBlock(MultiBlock multiBlock);
    void removeMultiBlock(MultiBlock multiBlock);
    void load(CompoundNBT tag);
    void save(CompoundNBT tag);
    void onUnload();
    void onWatch(ServerPlayerEntity player);
    void onUnWatch(ServerPlayerEntity player);
    void sendToWatchingPlayers(IZYMessage message);
    void markDirty();
    Chunk parent();
    ChunkPos pos();
}
