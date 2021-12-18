package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.client.gui.screen.inventory.TankScreen;
import nikita488.zycraft.menu.FabricatorMenu;
import nikita488.zycraft.menu.TankMenu;

public class ZYMenus
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final MenuEntry<FabricatorMenu> FABRICATOR = REGISTRATE.menu("fabricator", FabricatorMenu::new, () -> FabricatorScreen::new)
            .register();
    public static final MenuEntry<TankMenu> TANK = REGISTRATE.menu("tank", (MenuBuilder.ForgeMenuFactory<TankMenu>)TankMenu::new, () -> TankScreen::new)
            .register();

    public static void init() {}
}
