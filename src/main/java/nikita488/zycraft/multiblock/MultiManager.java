package nikita488.zycraft.multiblock;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.entity.MultiEntity;
import nikita488.zycraft.event.PlayerLoadedChunkEvent;
import nikita488.zycraft.event.PostLoadChunkEvent;
import nikita488.zycraft.event.UnloadChunkEvent;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.network.AddMultiPacket;
import nikita488.zycraft.network.RemoveMultiPacket;
import nikita488.zycraft.network.UpdateMultiPacket;

import javax.annotation.Nullable;
import java.util.Iterator;

public class MultiManager extends SavedData
{
    public static final String ID = ZYCraft.string("multi_blocks");
    public static final String MULTI_BLOCKS_TAG = ZYCraft.string("MultiBlocks");
    private static MultiManager instance = new MultiManager();
    private final Long2ObjectMap<ObjectSet<MultiBlock>> chunkMultiBlocks = new Long2ObjectOpenHashMap<>();
    private final Int2ObjectMap<MultiBlock> loadedMultiBlocks = new Int2ObjectOpenHashMap<>();
    private final ObjectSet<MultiBlock> pendingMultiBlocks = new ObjectOpenHashSet<>();
    private final ObjectList<MultiBlock> deferredMultiBlocks = new ObjectArrayList<>();
    private final ObjectList<MultiBlock> multiBlocksToSend = new ObjectArrayList<>();
    private final ObjectList<MultiBlock> multiBlocksToUpdate = new ObjectArrayList<>();
    private boolean updatingPendingMultiBlocks;

    public static void commonSetup()
    {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(MultiManager::onWorldTick);
        bus.addListener(MultiManager::onPostLoadChunk);
        bus.addListener(MultiManager::onPlayerLoadedChunk);
        bus.addListener(MultiManager::onChunkDataSave);
        bus.addListener(MultiManager::onChunkUnload);
        bus.addListener(MultiManager::onTeleportCommand);
        bus.addListener(MultiManager::onSpreadPlayersCommand);
    }

    public static void clientSetup()
    {
        IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(MultiManager::onClientTick);
        bus.addListener(MultiManager::onWorldUnload);
    }

    public static MultiManager getInstance()
    {
        return instance;
    }

    public static MultiManager getInstance(LevelAccessor accessor)
    {
        return !accessor.isClientSide() ? ((ServerLevel)accessor).getDataStorage().computeIfAbsent(tag -> null, MultiManager::new, ID) : instance;
    }

    @Nullable
    public MultiBlock getMultiBlock(int id)
    {
        return loadedMultiBlocks.get(id);
    }

    @Nullable
    private ObjectSet<MultiBlock> getMultiFromChunk(ChunkPos pos)
    {
        return chunkMultiBlocks.get(pos.toLong());
    }

    private boolean addMultiToChunk(ChunkPos chunkPos, MultiBlock multiBlock)
    {
        return chunkMultiBlocks.computeIfAbsent(chunkPos.toLong(), key -> new ObjectOpenHashSet<>()).add(multiBlock);
    }

    private void removeMultiFromChunk(ChunkPos chunkPos, MultiBlock multiBlock)
    {
        ObjectSet<MultiBlock> multiBlocks = chunkMultiBlocks.get(chunkPos.toLong());

        if (multiBlocks == null)
            return;

        if (multiBlocks.remove(multiBlock) && multiBlock.isMainChunk(chunkPos))
            multiBlock.setUnsaved();

        if (multiBlocks.isEmpty())
            chunkMultiBlocks.remove(chunkPos.toLong());
    }

    public void addMultiBlock(MultiBlock multiBlock, MultiBlock.AddingReason reason)
    {
        ZYCraft.LOGGER.debug("{} {}", reason.isFormed() ? "Adding" : "Loading", multiBlock);

        if (!multiBlock.isLoaded())
        {
            addPendingMultiBlock(multiBlock);
            return;
        }

        multiBlock.validate(reason);

        if (!multiBlock.isValid())
            return;

        loadedMultiBlocks.put(multiBlock.id(), multiBlock);

        for (ChunkPos pos : multiBlock.parentChunks())
            if (addMultiToChunk(pos, multiBlock) && multiBlock.isMainChunk(pos))
                multiBlock.setUnsaved();

        if (multiBlock.isClientSide())
            return;

        if (reason.isFormed())
            multiBlocksToSend.add(multiBlock);//Defer packet sending here because child block entities is not available on client yet

        if (multiBlock instanceof IDynamicMultiBlock dynamic)
            multiBlock.level().addFreshEntity(new MultiEntity(multiBlock.level(), dynamic, multiBlock.id()));
    }

    public void removeMultiBlock(MultiBlock multiBlock, MultiBlock.RemovalReason reason)
    {
        ZYCraft.LOGGER.debug("{} {}", reason.isDestroyed() ? "Removing" : "Unloading", multiBlock);

        if (!pendingMultiBlocks.isEmpty() && pendingMultiBlocks.remove(multiBlock))
        {
            ZYCraft.LOGGER.debug("{} was pending", multiBlock);
            return;
        }

        if (!loadedMultiBlocks.isEmpty() && loadedMultiBlocks.remove(multiBlock.id()) == null)
        {
            ZYCraft.LOGGER.debug("{} was not present", multiBlock);
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks())
            removeMultiFromChunk(pos, multiBlock);

        if (!multiBlock.isClientSide() && reason.isDestroyed())
            multiBlock.sendToTracking(new RemoveMultiPacket(multiBlock));

        multiBlock.invalidate(reason);
    }

    private void addPendingMultiBlock(MultiBlock multiBlock)
    {
        if (multiBlock.isValid() && !loadedMultiBlocks.isEmpty() && loadedMultiBlocks.remove(multiBlock.id()) == null)
            return;

        ZYCraft.LOGGER.debug("Adding pending {}", multiBlock);
        (updatingPendingMultiBlocks ? deferredMultiBlocks : pendingMultiBlocks).add(multiBlock);

        if (!multiBlock.isValid())
        {
            addMultiToChunk(multiBlock.mainChunk(), multiBlock);
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks())
            if (!multiBlock.isMainChunk(pos))
                removeMultiFromChunk(pos, multiBlock);

        multiBlock.invalidate(MultiBlock.RemovalReason.UNLOADED);
    }

    public void sendMultiUpdated(MultiBlock multiBlock)
    {
        if (!multiBlock.isChanged())
            multiBlock.setChanged(multiBlocksToUpdate.add(multiBlock));
    }

    private void sendPackets()
    {
        if (!multiBlocksToSend.isEmpty())
        {
            for (MultiBlock multiBlock : multiBlocksToSend)
                multiBlock.sendToTracking(new AddMultiPacket(multiBlock));

            multiBlocksToSend.clear();
        }

        if (!multiBlocksToUpdate.isEmpty())
        {
            for (MultiBlock multiBlock : multiBlocksToUpdate)
            {
                multiBlock.sendToTracking(new UpdateMultiPacket(multiBlock));
                multiBlock.setChanged(false);
            }

            multiBlocksToUpdate.clear();
        }
    }

    private void updatePendingMultiBlocks()
    {
        if (pendingMultiBlocks.isEmpty())
            return;

        this.updatingPendingMultiBlocks = true;

        for (Iterator<MultiBlock> iterator = pendingMultiBlocks.iterator(); iterator.hasNext();)
        {
            MultiBlock multiBlock = iterator.next();

            if (!multiBlock.isLoaded())
                continue;

            addMultiBlock(multiBlock, MultiBlock.AddingReason.LOADED);
            iterator.remove();
        }

        this.updatingPendingMultiBlocks = false;

        if (!deferredMultiBlocks.isEmpty())
        {
            pendingMultiBlocks.addAll(deferredMultiBlocks);
            deferredMultiBlocks.clear();
        }
    }

    private void tick()
    {
        updatePendingMultiBlocks();
        sendPackets();
    }

    private static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            getInstance(event.world).tick();
    }

    private static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            instance.tick();
    }

    private static void onWorldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().isClientSide())
            instance = new MultiManager();
    }

    private static void onPostLoadChunk(PostLoadChunkEvent event)
    {
        CompoundTag tag = event.getChunkTag();

        if (!tag.contains(MULTI_BLOCKS_TAG, Tag.TAG_LIST))
            return;

        ListTag multiTags = tag.getList(MULTI_BLOCKS_TAG, Tag.TAG_COMPOUND);
        Level level = (Level)event.getWorld();
        MultiManager manager = getInstance(level);
        ChunkPos pos = event.getChunk().getPos();

        for (int i = 0; i < multiTags.size(); i++)
            MultiType.create(level, pos, multiTags.getCompound(i)).ifPresent(manager::addPendingMultiBlock);
    }

    private static void onPlayerLoadedChunk(PlayerLoadedChunkEvent event)
    {
        MultiManager manager = getInstance(event.getWorld());
        ChunkPos pos = event.getChunk().getPos();
        ObjectSet<MultiBlock> multiBlocks = manager.getMultiFromChunk(pos);

        if (multiBlocks != null)
            for (MultiBlock multiBlock : multiBlocks)
                if (multiBlock.isMainChunk(pos))
                    if (multiBlock.isValid())
                        ZYCraft.CHANNEL.send(PacketDistributor.PLAYER.with(event::getPlayer), new AddMultiPacket(multiBlock));
                    else
                        manager.multiBlocksToSend.add(multiBlock);
    }

    private static void onChunkDataSave(ChunkDataEvent.Save event)
    {
        ChunkPos pos = event.getChunk().getPos();
        ObjectSet<MultiBlock> multiBlocks = getInstance(event.getWorld()).getMultiFromChunk(pos);

        if (multiBlocks == null)
            return;

        ListTag multiTags = new ListTag();

        for (MultiBlock multiBlock : multiBlocks)
        {
            if (!multiBlock.isMainChunk(pos))
                continue;

            ResourceLocation id = ZYRegistries.MULTI_TYPES.get().getKey(multiBlock.type());

            if (id == null)
            {
                ZYCraft.LOGGER.error("{} doesn't have a registry name. Data will not be saved", multiBlock);
                continue;
            }

            CompoundTag multiTag = new CompoundTag();

            multiTag.putString("ID", id.toString());
            multiBlock.save(multiTag);
            multiTags.add(multiTag);
        }

        if (!multiTags.isEmpty())
            event.getData().put(MULTI_BLOCKS_TAG, multiTags);
    }

    private static void onChunkUnload(UnloadChunkEvent event)
    {
        MultiManager manager = getInstance(event.getWorld());
        ChunkPos pos = event.getChunk().getPos();
        ObjectSet<MultiBlock> multiBlocks = manager.getMultiFromChunk(pos);

        if (multiBlocks != null)
            for (MultiBlock multiBlock : multiBlocks)
                if (multiBlock.isMainChunk(pos))
                    manager.removeMultiBlock(multiBlock, MultiBlock.RemovalReason.UNLOADED);
                else
                    manager.addPendingMultiBlock(multiBlock);

        manager.chunkMultiBlocks.remove(pos.toLong());//TODO: Needed?
    }

    private static void onTeleportCommand(EntityTeleportEvent.TeleportCommand event)
    {
        if (event.getEntity() instanceof MultiEntity)
            event.setCanceled(true);
    }

    private static void onSpreadPlayersCommand(EntityTeleportEvent.SpreadPlayersCommand event)
    {
        if (event.getEntity() instanceof MultiEntity)
            event.setCanceled(true);
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        return tag;
    }
}
