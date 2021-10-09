package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.ContainerEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.client.gui.screen.inventory.TankScreen;
import nikita488.zycraft.menu.FabricatorContainer;
import nikita488.zycraft.menu.TankContainer;

public class ZYContainers
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final ContainerEntry<FabricatorContainer> FABRICATOR = REGISTRATE.container("fabricator", FabricatorContainer::new, () -> FabricatorScreen::new)
            .register();
    public static final ContainerEntry<TankContainer> TANK = REGISTRATE.container("tank", (ContainerBuilder.ForgeContainerFactory<TankContainer>)TankContainer::new, () -> TankScreen::new)
            .register();

    public static void init() {}
}
