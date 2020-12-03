package nikita488.zycraft.multiblock;

import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID)
public class MultiEventHandler
{
    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            MultiManager.getInstance(event.world).tick();
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event)
    {
        MultiManager.getInstance(event.getWorld()).onChunkUnload(event.getChunk().getPos());
    }

    @SubscribeEvent
    public static void onChunkDataLoad(ChunkDataEvent.Load event)
    {
        if (event.getStatus() == ChunkStatus.Type.LEVELCHUNK)
            MultiManager.getInstance(event.getWorld()).onChunkDataLoad((ServerWorld)event.getWorld(), event.getChunk().getPos(), event.getData().getCompound("Level"));
    }

    @SubscribeEvent
    public static void onChunkDataSave(ChunkDataEvent.Save event)
    {
        MultiManager.getInstance(event.getWorld()).onChunkDataSave(event.getChunk().getPos(), event.getData().getCompound("Level"));
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        MultiManager.getInstance(event.getWorld()).onChunkWatch(event.getPos(), event.getPlayer());
    }

    @SubscribeEvent
    public static void onChunkUnWatch(ChunkWatchEvent.UnWatch event)
    {
        MultiManager.getInstance(event.getWorld()).onChunkUnWatch(event.getPos(), event.getPlayer());
    }
}
