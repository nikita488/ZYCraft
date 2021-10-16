package nikita488.zycraft.util;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID)
public class DataPackReloadCounter extends SimplePreparableReloadListener<Unit>
{
    public static final DataPackReloadCounter INSTANCE = new DataPackReloadCounter();
    private int count;

    @SubscribeEvent
    public static void init(AddReloadListenerEvent event)
    {
        event.addListener(INSTANCE);
    }

    @Override
    protected Unit prepare(ResourceManager manager, ProfilerFiller profiler)
    {
        return Unit.INSTANCE;
    }

    @Override
    protected void apply(Unit unit, ResourceManager manager, ProfilerFiller profiler)
    {
        this.count++;
    }

    public int count()
    {
        return count;
    }
}
