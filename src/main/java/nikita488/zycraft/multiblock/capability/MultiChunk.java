package nikita488.zycraft.multiblock.capability;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.capability.IMultiChunk;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.multiblock.MultiCapabilities;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;
import nikita488.zycraft.multiblock.network.AddMultiPacket;
import nikita488.zycraft.network.IZYMessage;
import nikita488.zycraft.network.ZYChannel;

public class MultiChunk implements IMultiChunk
{
    private final Chunk chunk;
    private final ChunkPos pos;
    private final IMultiWorld world;
    private final ObjectArrayList<MultiBlock> multiBlocks = new ObjectArrayList<>();

    public MultiChunk(Chunk chunk)
    {
        this.chunk = chunk;
        this.pos = chunk.getPos();
        this.world = MultiCapabilities.getMultiWorld(chunk.getWorld()).orElse(null);
    }

    @Override
    public void addMultiBlock(MultiBlock multiBlock)
    {
        multiBlocks.add(multiBlock);
    }

    @Override
    public void removeMultiBlock(MultiBlock multiBlock)
    {
        multiBlocks.remove(multiBlock);
    }

    @Override
    public void load(CompoundNBT tag)
    {
        if (!tag.contains("MultiBlocks"))
            return;

        ListNBT multiTags = tag.getList("MultiBlocks", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < multiTags.size(); i++)
        {
            MultiBlock multiBlock = MultiBlock.create(multiTags.getCompound(i));
            if (multiBlock == null) continue;
            multiBlock.setLocation(world, this);
            world.deferMultiBlock(multiBlock);
        }
    }

    @Override
    public void save(CompoundNBT tag)
    {
        ListNBT multiTags = new ListNBT();

        for (MultiBlock multiBlock : multiBlocks)
        {
            if (!multiBlock.isMainChunk(pos))
                continue;

            CompoundNBT multiTag = new CompoundNBT();
            multiTag.putString("Type", multiBlock.type().getRegistryName().toString());
            multiBlock.save(multiTag);
            multiTags.add(multiTag);
        }

        if (!multiTags.isEmpty())
            tag.put("MultiBlocks", multiTags);
    }

    @Override
    public void onUnload()
    {
        for (MultiBlock multiBlock : multiBlocks)
            if (multiBlock.isMainChunk(pos))
                world.removeMultiBlock(multiBlock, MultiInvalidationType.UNLOAD);
            else
                world.deferMultiBlock(multiBlock);
    }

    @Override
    public void onWatch(ServerPlayerEntity player)
    {
        for (MultiBlock multiBlock : multiBlocks)
            if (multiBlock.valid() && multiBlock.isMainChunk(pos))
            {
                sendToWatchingPlayers(new AddMultiPacket(multiBlock));
                ZYCraft.LOGGER.warn("Send Add packet to {} players while watching.", ((ServerChunkProvider)world.parent().getChunkProvider()).chunkManager.getTrackingPlayers(multiBlock.mainChunk().pos(), false).count());
            }
    }

    @Override
    public void onUnWatch(ServerPlayerEntity player)
    {

    }

    @Override
    public void sendToWatchingPlayers(IZYMessage message)
    {
        ZYChannel.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(this::parent), message);
    }

    @Override
    public void markDirty()
    {
        chunk.markDirty();
    }

    @Override
    public Chunk parent()
    {
        return chunk;
    }

    @Override
    public ChunkPos pos()
    {
        return pos;
    }
}
