package nikita488.zycraft.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.MultiEntity;
import nikita488.zycraft.multiblock.client.MultiEntityRenderer;

public class ZYEntities
{
    public static final EntityEntry<MultiEntity> MULTI_BLOCK = ZYCraft.registrate().<MultiEntity>entity("multi_block", MultiEntity::new, EntityClassification.MISC)
            .properties(properties -> properties
                    .setTrackingRange(4)
                    .setUpdateInterval(20)
                    .size(1, 1)
                    .setShouldReceiveVelocityUpdates(false)
                    .disableSerialization()
                    .disableSummoning()
                    .immuneToFire())
            .lang("")
            .renderer(() -> MultiEntityRenderer::new)
            .register();

    public static void init() {}
}
