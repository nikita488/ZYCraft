package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.ContainerEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.inventory.container.FabricatorContainer;

public class ZYContainers
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final ContainerEntry<FabricatorContainer> FABRICATOR = REGISTRATE.container("fabricator", (ContainerBuilder.ContainerFactory<FabricatorContainer>)FabricatorContainer::new, () -> FabricatorScreen::new)
            .register();

    public static void init() {}
}
