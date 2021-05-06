package nikita488.zycraft.util;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Unit;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nikita488.zycraft.ZYCraft;

@Mod.EventBusSubscriber(modid = ZYCraft.MOD_ID)
public class DataPackReloadCounter extends ReloadListener<Unit>
{
    public static final DataPackReloadCounter INSTANCE = new DataPackReloadCounter();
    private int count;

    @SubscribeEvent
    public static void init(AddReloadListenerEvent event)
    {
        event.addListener(INSTANCE);
    }

    @Override
    protected Unit prepare(IResourceManager manager, IProfiler profiler)
    {
        return Unit.INSTANCE;
    }

    @Override
    protected void apply(Unit unit, IResourceManager manager, IProfiler profiler)
    {
        this.count++;
    }

    public int count()
    {
        return count;
    }
}
