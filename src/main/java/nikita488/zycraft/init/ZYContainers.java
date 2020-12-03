package nikita488.zycraft.init;

import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.ContainerEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.multiblock.tank.TankContainer;
import nikita488.zycraft.multiblock.tank.TankScreen;

public class ZYContainers
{
    public static final ContainerEntry<TankContainer> TANK = ZYCraft.registrate()
            .container("tank", (ContainerBuilder.ForgeContainerFactory<TankContainer>)TankContainer::new, () -> TankScreen::new)
            .register();

    public static void init() {}
}
