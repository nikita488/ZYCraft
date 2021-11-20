package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.ContainerEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.client.gui.screen.inventory.TankScreen;
import nikita488.zycraft.menu.FabricatorMenu;
import nikita488.zycraft.menu.TankMenu;

public class ZYMenus
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final ContainerEntry<FabricatorMenu> FABRICATOR = REGISTRATE.container("fabricator", FabricatorMenu::new, () -> FabricatorScreen::new)
            .register();
    public static final ContainerEntry<TankMenu> TANK = REGISTRATE.container("tank", (ContainerBuilder.ForgeContainerFactory<TankMenu>)TankMenu::new, () -> TankScreen::new)
            .register();

    public static void init() {}
}
