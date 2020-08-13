package nikita488.zycraft.api.multiblock.capability;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.multiblock.network.RemoveMultiPacket;
import nikita488.zycraft.multiblock.network.UpdateMultiPacket;

import javax.annotation.Nullable;

public interface IMultiWorld
{
    void addMultiBlock(MultiBlock multiBlock, MultiValidationType type);

    void removeMultiBlock(MultiBlock multiBlock, MultiInvalidationType type);

    void deferMultiBlock(MultiBlock multiBlock);

    void scheduleMultiUpdate(MultiBlock multiBlock);

    void handleAddPacket(AddMultiPacket packet);

    void handleRemovePacket(RemoveMultiPacket packet);

    void handleUpdatePacket(UpdateMultiPacket packet);

    void tick();

    MultiBlock getMultiBlock(int id);

    @Nullable
    MultiChildTile getMultiChild(BlockPos pos);

    boolean isChunkLoaded(ChunkPos pos);

    LazyOptional<IMultiChunk> getChunk(ChunkPos pos);

    LazyOptional<IMultiChunk> getChunk(IChunk chunk);

    boolean isRemote();

    World parent();
}
