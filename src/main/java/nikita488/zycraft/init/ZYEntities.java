package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.renderer.entity.MultiEntityRenderer;
import nikita488.zycraft.entity.MultiEntity;

public class ZYEntities
{
    public static final EntityEntry<MultiEntity> MULTI_BLOCK = ZYCraft.registrate().<MultiEntity>entity("multi_block", MultiEntity::new, MobCategory.MISC)
            .properties(properties -> properties
                    .setTrackingRange(10)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .sized(0F, 0F)
                    .setShouldReceiveVelocityUpdates(false)
                    .noSave()
                    .noSummon()
                    .fireImmune())
            .lang("")
            .renderer(() -> MultiEntityRenderer::new)
            .register();

    public static void init() {}
}
