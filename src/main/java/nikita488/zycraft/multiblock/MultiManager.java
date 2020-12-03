package nikita488.zycraft.multiblock;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.multiblock.network.RemoveMultiPacket;
import nikita488.zycraft.multiblock.network.UpdateMultiPacket;
import nikita488.zycraft.network.ZYChannel;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MultiManager extends WorldSavedData
{
    public static final String NAME = ZYCraft.MOD_ID + ":multiblocks";
    public static final MultiManager CLIENT_INSTANCE = new MultiManager();
    private final Int2ObjectMap<MultiBlock> multiBlocks = new Int2ObjectOpenHashMap<>();
    private final ObjectSet<MultiBlock> deferredMultiBlocks = new ObjectOpenHashSet<>();
    private final List<MultiBlock> multiBlocksToSend = new LinkedList<>();
    private final ObjectList<MultiBlock> multiBlocksToUpdate = new ObjectArrayList<>();
    private final Long2ObjectMap<ObjectSet<MultiBlock>> chunks = new Long2ObjectOpenHashMap<>();

    public MultiManager()
    {
        super(NAME);
    }

    public static MultiManager getInstance(IWorld world)
    {
        if (world.isRemote())
            return CLIENT_INSTANCE;
        return ((ServerWorld)world).getSavedData().getOrCreate(MultiManager::new, NAME);
    }

    @Nullable
    public MultiBlock getMultiBlock(int id)
    {
        return multiBlocks.get(id);
    }

    public void addMultiBlock(MultiBlock multiBlock)
    {
        //TODO: When player starts to watch a chunk, it's still not loaded
        //System.out.println(multiBlock.world().getChunkProvider().chunkExists(multiBlock.mainChunk.x, multiBlock.mainChunk.z));

        if (multiBlocks.containsKey(multiBlock.id()))
            return;

        if (!multiBlock.isLoaded())
        {
            deferMultiBlock(multiBlock);//TODO: Decide how to remove from deferred list on client when player unwatch chunk
            return;
        }

        multiBlock.validate();

        if (!multiBlock.isValid())
            return;

        World world = multiBlock.world();

        if (!world.isRemote)
        {
            MultiEntity multi = new MultiEntity(multiBlock);
            BlockPos origin = multiBlock.childBlocks.get(0);
            multi.setPosition(origin.getX(), origin.getY(), origin.getZ());
            world.addEntity(multi);
            ZYCraft.LOGGER.warn("Added MB entity on Server: {}", world.getGameTime());
        }

        for (ChunkPos pos : multiBlock.parentChunks)
            addMultiBlockToChunk(pos, multiBlock);

        multiBlocks.put(multiBlock.id(), multiBlock);

        if (!world.isRemote)
        {
            multiBlocksToSend.add(multiBlock);//TODO: Do we really need to defer packet sending?
            ZYCraft.LOGGER.info("Send initial add packet to {} players", ((ServerChunkProvider)multiBlock.world().getChunkProvider()).chunkManager.getTrackingPlayers(multiBlock.mainChunk(), false).count());
        }
    }

    public void removeMultiBlock(MultiBlock multiBlock, boolean fromChunk)
    {
        if (deferredMultiBlocks.remove(multiBlock) || multiBlocks.remove(multiBlock.id()) == null)
            return;

        for (ChunkPos pos : multiBlock.parentChunks)
            if (fromChunk || !multiBlock.isMainChunk(pos))
                removeMultiBlockFromChunk(pos, multiBlock);

        if (!multiBlock.world().isRemote && fromChunk)
            multiBlock.sendToTracking(new RemoveMultiPacket(multiBlock));

        multiBlock.invalidate();
    }

    private void deferMultiBlock(MultiBlock multiBlock)
    {
        if (multiBlock.isValid() && multiBlocks.remove(multiBlock.id()) == null)
            return;

        deferredMultiBlocks.add(multiBlock);

        if (!multiBlock.isValid())
        {
            addMultiBlockToChunk(multiBlock.mainChunk(), multiBlock);
            return;
        }

        for (ChunkPos pos : multiBlock.parentChunks)
            if (!multiBlock.isMainChunk(pos))
                removeMultiBlockFromChunk(pos, multiBlock);

        if (!multiBlock.world().isRemote)
            multiBlock.sendToTracking(new RemoveMultiPacket(multiBlock));

        multiBlock.invalidate();
    }

    public void scheduleMultiUpdate(MultiBlock multiBlock)
    {
        if (multiBlock.needsUpdate)
            return;

        multiBlocksToUpdate.add(multiBlock);
        multiBlock.needsUpdate = true;
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
                multiBlock.needsUpdate = false;
            }

            multiBlocksToUpdate.clear();
        }
    }

    private void updateDeferred()
    {
        for (Iterator<MultiBlock> iterator = deferredMultiBlocks.iterator(); iterator.hasNext();)
        {
            MultiBlock multiBlock = iterator.next();

            if (!multiBlock.isLoaded())
                continue;

            addMultiBlock(multiBlock);
            iterator.remove();
        }
    }

    public void tick()
    {
        sendPackets();
        updateDeferred();
    }

    private void addMultiBlockToChunk(ChunkPos pos, MultiBlock multiBlock)
    {
        chunks.computeIfAbsent(pos.asLong(), key -> new ObjectOpenHashSet<>()).add(multiBlock);
    }

    private void removeMultiBlockFromChunk(ChunkPos pos, MultiBlock multiBlock)
    {
        ObjectSet<MultiBlock> multiBlocks = chunks.get(pos.asLong());

        if (multiBlocks == null)
            return;

        multiBlocks.remove(multiBlock);

        if (multiBlocks.isEmpty())
            chunks.remove(pos.asLong());
    }

    public void onChunkUnload(ChunkPos pos)
    {
        ObjectSet<MultiBlock> multiBlocks = chunks.get(pos.asLong());

        if (multiBlocks == null)
            return;

        for (MultiBlock multiBlock : multiBlocks)
        {
            if (multiBlock.isMainChunk(pos))//TODO: maybe check if world is server world here
                removeMultiBlock(multiBlock, false);
            else
                deferMultiBlock(multiBlock);
        }
    }

    public void onChunkDataLoad(World world, ChunkPos pos, CompoundNBT tag)
    {
        if (!tag.contains("ZYMultiBlocks"))
            return;

        ListNBT multiTags = tag.getList("ZYMultiBlocks", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < multiTags.size(); i++)
        {
            MultiBlock multiBlock = MultiBlock.create(world, pos, multiTags.getCompound(i));

            if (multiBlock != null)
                deferMultiBlock(multiBlock);
        }
    }

    public void onChunkDataSave(ChunkPos pos, CompoundNBT tag)
    {
        ObjectSet<MultiBlock> multiBlocks = chunks.get(pos.asLong());

        if (multiBlocks == null)
            return;

        ListNBT multiTags = new ListNBT();

        for (MultiBlock multiBlock : multiBlocks)
        {
            if (!multiBlock.isMainChunk(pos))
                continue;

            ResourceLocation name = multiBlock.getType().getRegistryName();

            if (name == null)
            {
                LogManager.getLogger().error("The MultiBlock {} has no registry name. Data will not be saved", multiBlock.getClass().getName());
                continue;
            }

            CompoundNBT multiTag = new CompoundNBT();

            multiTag.putString("Name", name.toString());
            multiBlock.save(multiTag);
            multiTags.add(multiTag);
        }

        if (!multiTags.isEmpty())
            tag.put("ZYMultiBlocks", multiTags);
    }

    public void onChunkWatch(ChunkPos pos, ServerPlayerEntity player)
    {
        ObjectSet<MultiBlock> multiBlocks = chunks.get(pos.asLong());

        if (multiBlocks == null)
            return;

        for (MultiBlock multiBlock : multiBlocks)
            if (multiBlock.isValid() && multiBlock.isMainChunk(pos))
                ZYChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new AddMultiPacket(multiBlock));
    }

    public void onChunkUnWatch(ChunkPos pos, ServerPlayerEntity player)
    {
        ObjectSet<MultiBlock> multiBlocks = chunks.get(pos.asLong());

        if (multiBlocks == null)
            return;

        for (MultiBlock multiBlock : multiBlocks)
        {
            ZYCraft.LOGGER.warn("Unwatch {}", multiBlock.mainChunk);
        }
    }

    @Override
    public void read(CompoundNBT tag) {}

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        return tag;
    }
}
