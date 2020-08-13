package nikita488.zycraft.multiblock;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.multiblock.capability.IMultiChunk;
import nikita488.zycraft.api.multiblock.capability.IMultiWorld;
import nikita488.zycraft.multiblock.capability.MultiChunkCapability;
import nikita488.zycraft.multiblock.capability.MultiWorldCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID)
public class MultiCapabilities
{
    private static final Int2ObjectOpenHashMap<LazyOptional<IMultiWorld>> worlds = new Int2ObjectOpenHashMap<>();

    @Nonnull
    public static LazyOptional<IMultiWorld> getMultiWorld(@Nullable IWorld world)
    {
        if (world == null)
            return LazyOptional.empty();

        if (world.isRemote())
            return MultiClientCapabilities.getMultiWorld(world);

        int id = world.getDimension().getType().getId();
        LazyOptional<IMultiWorld> capability = worlds.getOrDefault(id, LazyOptional.empty());

        if (capability.isPresent())
            return capability;

        ZYCraft.LOGGER.info("Cache ServerWorld {} capability.", id);
        capability = world.getWorld().getCapability(MultiWorldCapability.INSTANCE);
        capability.addListener(cap -> worlds.remove(id));
        worlds.put(id, capability);
        return capability;
    }

    @SubscribeEvent
    public static void initMultiWorld(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(ZYCraft.modLoc("multi_world"), new MultiWorldCapability.Provider(event.getObject()));
    }

    @SubscribeEvent
    public static void initMultiChunk(AttachCapabilitiesEvent<Chunk> event)
    {
        if (!event.getObject().isEmpty())
            event.addCapability(ZYCraft.modLoc("multi_chunk"), new MultiChunkCapability.Provider(event.getObject()));
    }

    @SubscribeEvent
    public static void invalidateMultiWorld(WorldEvent.Unload event)
    {
        getMultiWorld(event.getWorld()).invalidate();
        ZYCraft.LOGGER.info("Unload {}World {} capability.", event.getWorld().isRemote() ? "Client" : "Server", event.getWorld().getDimension().getType().getId());
    }

    @SubscribeEvent
    public static void tickMultiWorld(TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        getMultiWorld(event.world).ifPresent(IMultiWorld::tick);
    }

    @SubscribeEvent
    public static void unloadMultiChunk(ChunkEvent.Unload event)
    {
        LazyOptional<IMultiChunk> capability = getMultiWorld(event.getWorld())
                .map(world -> world.getChunk(event.getChunk()))
                .orElse(LazyOptional.empty());
        capability.ifPresent(IMultiChunk::onUnload);
        capability.invalidate();
    }

    @SubscribeEvent
    public static void watchMultiChunk(ChunkWatchEvent.Watch event)
    {
        LazyOptional<IMultiChunk> capability = getMultiWorld(event.getWorld())
                .map(world -> world.getChunk(event.getPos()))
                .orElse(LazyOptional.empty());
        capability.ifPresent(chunk -> chunk.onWatch(event.getPlayer()));
    }

    @SubscribeEvent
    public static void unwatchMultiChunk(ChunkWatchEvent.UnWatch event)
    {
        LazyOptional<IMultiChunk> capability = getMultiWorld(event.getWorld())
                .map(world -> world.getChunk(event.getPos()))
                .orElse(LazyOptional.empty());
        capability.ifPresent(chunk -> chunk.onUnWatch(event.getPlayer()));
    }
}
