package nikita488.zycraft.compat.jei;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FabricatorGuiContainerHandler implements IGuiContainerHandler<FabricatorScreen>
{
    @Override
    public List<Rect2i> getGuiExtraAreas(FabricatorScreen screen)
    {
        return Collections.singletonList(new Rect2i(screen.getGuiLeft() - 34, screen.getGuiTop() + 23, 34, 80));
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(FabricatorScreen screen, double mouseX, double mouseY)
    {
        ObjectList<IGuiClickableArea> areas = new ObjectArrayList<>();

        areas.add(IGuiClickableArea.createBasic(86, 29, 20, 8, RecipeTypes.CRAFTING));
        areas.add(IGuiClickableArea.createBasic(86, 65, 20, 8, RecipeTypes.CRAFTING));
        return areas;
    }
}
