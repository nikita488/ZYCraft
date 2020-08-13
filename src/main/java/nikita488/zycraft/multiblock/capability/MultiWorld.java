package nikita488.zycraft.multiblock.capability;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.common.util.LazyOptional;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.capability.IMultiChunk;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.multiblock.network.RemoveMultiPacket;
import nikita488.zycraft.multiblock.network.UpdateMultiPacket;

import javax.annotation.Nullable;
import java.util.Iterator;

public class MultiWorld implements IMultiWorld
{
    private final World world;
    private final Int2ObjectOpenHashMap<MultiBlock> multiBlocks = new Int2ObjectOpenHashMap<>();
    private final ObjectArrayList<MultiBlock> deferredMultiBlocks = new ObjectArrayList<>();
    private final ObjectArrayList<MultiBlock> multiBlocksToSend = new ObjectArrayList<>();
    private final ObjectArrayList<MultiBlock> multiBlocksToUpdate = new ObjectArrayList<>();
    private final Int2ObjectOpenHashMap<AddMultiPacket> deferredPackets = new Int2ObjectOpenHashMap<>();
    private final Long2ObjectOpenHashMap<LazyOptional<IMultiChunk>> chunks = new Long2ObjectOpenHashMap<>();

    public MultiWorld(World world)
    {
        this.world = world;
    }

    @Override
    public void addMultiBlock(MultiBlock multiBlock, MultiValidationType type)
    {
        ZYCraft.LOGGER.warn("Add MultiBlock {}.", multiBlock.id());

        if (multiBlocks.containsKey(multiBlock.id()))
        {
            ZYCraft.LOGGER.error("MultiBlock {} already exist.", multiBlock.id());
            return;
        }

        if (!multiBlock.isLoaded())
        {
            ZYCraft.LOGGER.error("MultiBlock {} isn't loaded. Deferring.", multiBlock.id());
            deferMultiBlock(multiBlock);
            return;
        }

        multiBlock.validate(type);

        if (!multiBlock.valid())
        {
            ZYCraft.LOGGER.error("MultiBlock {} isn't valid.", multiBlock.id());
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks())
            if (type.shouldCreate() || !multiBlock.isMainChunk(pos))
                getChunk(pos).ifPresent(chunk -> chunk.addMultiBlock(multiBlock));

        multiBlocks.put(multiBlock.id(), multiBlock);

        if (!world.isRemote)
            multiBlocksToSend.add(multiBlock);
    }

    @Override
    public void removeMultiBlock(MultiBlock multiBlock, MultiInvalidationType type)
    {
        ZYCraft.LOGGER.warn("{} MultiBlock {}.", type.name(), multiBlock.id());

        if (deferredMultiBlocks.remove(multiBlock))
        {
            ZYCraft.LOGGER.warn("MultiBlock {} was deferred. Removing.", multiBlock.id());
            return;
        }

        if (multiBlocks.remove(multiBlock.id()) == null)
        {
            ZYCraft.LOGGER.error("MultiBlock {} was removed.", multiBlock.id());
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks())
            if (type.shouldDestroy() || !multiBlock.isMainChunk(pos))
                getChunk(pos).ifPresent(chunk -> chunk.removeMultiBlock(multiBlock));

        if (!world.isRemote && type.shouldDestroy())
        {
            multiBlock.mainChunk().sendToWatchingPlayers(new RemoveMultiPacket(multiBlock));
            ZYCraft.LOGGER.warn("Send Remove packet to {} players.",
                    ((ServerChunkProvider)world.getChunkProvider()).chunkManager.getTrackingPlayers(multiBlock.mainChunk().pos(), false).count());
        }

        multiBlock.invalidate(type);
    }

    @Override
    public void deferMultiBlock(MultiBlock multiBlock)
    {
        ZYCraft.LOGGER.warn("Deferring MultiBlock {}.", multiBlock.id());

        if (multiBlock.valid() && multiBlocks.remove(multiBlock.id()) == null) {
            ZYCraft.LOGGER.error("MultiBlock {} was valid and already removed.", multiBlock.id());
            return;
        }

        deferredMultiBlocks.add(multiBlock);

        if (!multiBlock.valid())
        {
            ZYCraft.LOGGER.warn("MultiBlock {} is invalid. Adding to parent chunk.", multiBlock.id());
            multiBlock.mainChunk().addMultiBlock(multiBlock);
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks())
            if (!multiBlock.isMainChunk(pos))
                getChunk(pos).ifPresent(chunk -> chunk.removeMultiBlock(multiBlock));

        multiBlock.invalidate(MultiInvalidationType.UNLOAD);
        //TODO: Is remove packet really needed?
    }

    @Override
    public void scheduleMultiUpdate(MultiBlock multiBlock)
    {
        if (world.isRemote || multiBlock.needsUpdate()) return;
        multiBlocksToUpdate.add(multiBlock);
        multiBlock.setNeedsUpdate(true);
    }

    @Override
    public void handleAddPacket(AddMultiPacket packet)
    {
        if (!isChunkLoaded(packet.parentChunk()))
        {
            deferredPackets.put(packet.id(), packet);
            return;
        }

        MultiBlock multiBlock = packet.type().get();
        if (multiBlock == null) return;
        multiBlock.setLocation(this, packet.parentChunk());
        multiBlock.setID(packet.id());
        multiBlock.decode(packet.buffer());
        multiBlock.initChildBlocks();
        addMultiBlock(multiBlock, MultiValidationType.CREATE);
    }

    @Override
    public void handleRemovePacket(RemoveMultiPacket packet)
    {
        MultiBlock multiBlock = multiBlocks.get(packet.id());
        if (multiBlock != null)
            removeMultiBlock(multiBlock, MultiInvalidationType.DESTROY);
    }

    @Override
    public void handleUpdatePacket(UpdateMultiPacket packet)
    {
        MultiBlock multiBlock = multiBlocks.get(packet.id());
        if (multiBlock != null)
            multiBlock.decodeUpdate(packet.buffer());
    }

    private void sendMultiBlocksToClient()
    {
        for (MultiBlock multiBlock : multiBlocksToSend)
        {
            multiBlock.mainChunk().sendToWatchingPlayers(new AddMultiPacket(multiBlock));

            ZYCraft.LOGGER.warn("Send Add packet to {} players.",
                    ((ServerChunkProvider)world.getChunkProvider()).chunkManager.getTrackingPlayers(multiBlock.mainChunk().pos(), false).count());
        }

        if (!multiBlocksToSend.isEmpty())
            multiBlocksToSend.clear();
    }

    private void updateDeferredMultiBlocks()
    {
        for (Iterator<MultiBlock> iterator = deferredMultiBlocks.iterator(); iterator.hasNext();)
        {
            MultiBlock multiBlock = iterator.next();
            ZYCraft.LOGGER.warn("MultiBlock {} right now deferred.", multiBlock.id());
            if (!multiBlock.isLoaded())
                continue;
            addMultiBlock(multiBlock, MultiValidationType.LOAD);
            iterator.remove();
        }
    }

    private void updateDeferredPackets()
    {
        for (Iterator<AddMultiPacket> iterator = deferredPackets.values().iterator(); iterator.hasNext();)
        {
            AddMultiPacket packet = iterator.next();
            ZYCraft.LOGGER.warn("MultiPacket {} right now deferred.", packet.parentChunk());
            if (!isChunkLoaded(packet.parentChunk()))
                continue;
            handleAddPacket(packet);
            iterator.remove();
        }
    }

    private void updateMultiBlocks()
    {
        for (MultiBlock multiBlock : multiBlocksToUpdate)
        {
            multiBlock.mainChunk().sendToWatchingPlayers(new UpdateMultiPacket(multiBlock));
            multiBlock.setNeedsUpdate(false);
        }

        if (!multiBlocksToUpdate.isEmpty())
            multiBlocksToUpdate.clear();
    }

    @Override
    public void tick()
    {
        sendMultiBlocksToClient();
        updateDeferredMultiBlocks();
        updateDeferredPackets();
        updateMultiBlocks();
    }

    @Override
    public MultiBlock getMultiBlock(int id)
    {
        return multiBlocks.get(id);
    }

    @Nullable
    @Override
    public MultiChildTile getMultiChild(BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof MultiChildTile)
            return (MultiChildTile)tile;

        return null;
    }

    @Override
    public boolean isChunkLoaded(ChunkPos pos)
    {
        return world.getChunkProvider().chunkExists(pos.x, pos.z);
    }

    @Override
    public LazyOptional<IMultiChunk> getChunk(ChunkPos pos)
    {
        return isChunkLoaded(pos) ? getChunk(world.getChunk(pos.x, pos.z)) : LazyOptional.empty();
    }

    @Override
    public LazyOptional<IMultiChunk> getChunk(IChunk chunk)
    {
        if (!(chunk instanceof Chunk))
            return LazyOptional.empty();

        long pos = chunk.getPos().asLong();
        LazyOptional<IMultiChunk> capability = chunks.getOrDefault(pos, LazyOptional.empty());

        if (capability.isPresent())
            return capability;

        capability = ((Chunk)chunk).getCapability(MultiChunkCapability.INSTANCE);
        capability.addListener(cap -> chunks.remove(pos));
        chunks.put(pos, capability);
        return capability;
    }

    @Override
    public boolean isRemote()
    {
        return world.isRemote;
    }

    @Override
    public World parent()
    {
        return world;
    }
}
