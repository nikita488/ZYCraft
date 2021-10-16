package nikita488.zycraft.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.client.gui.screen.inventory.FabricatorScreen;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYItems;

@JeiPlugin
public class ZYJEIPlugin implements IModPlugin
{
    public static final ResourceLocation ID = ZYCraft.id("jei_plugin");

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration)
    {
        registration.useNbtForSubtypes(ZYItems.ALUMINIUM_FOIL.get(), ZYItems.QUARTZ_BUCKET.get(), ZYItems.ALUMINIUM_CAN.get());
    }

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
        registration.addGuiContainerHandler(FabricatorScreen.class, new FabricatorGuiContainerHandler());
        registration.addGhostIngredientHandler(FabricatorScreen.class, new FabricatorGhostIngredientHandler());
    }

    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }
}
