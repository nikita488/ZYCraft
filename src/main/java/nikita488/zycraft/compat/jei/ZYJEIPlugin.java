package nikita488.zycraft.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.init.ZYBlocks;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class ZYJEIPlugin implements IModPlugin
{
    public static final ResourceLocation NAME = ZYCraft.id("jei_plugin");

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(new FabricatorRecipeTransferHandler(registration.getTransferHelper()), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(ZYBlocks.FABRICATOR.asStack(), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addGuiContainerHandler(FabricatorScreen.class, new IGuiContainerHandler<FabricatorScreen>()
        {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(FabricatorScreen screen)
            {
                return Collections.singletonList(new Rectangle2d(screen.getGuiLeft() - 34, screen.getGuiTop() + 23, 34, 80));
            }
        });
        registration.addGhostIngredientHandler(FabricatorScreen.class, new FabricatorGhostIngredientHandler());
    }

    @Override
    public ResourceLocation getPluginUid()
    {
        return NAME;
    }
}
