package nikita488.zycraft.compat.jei;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYMenus;
import nikita488.zycraft.menu.FabricatorMenu;
import nikita488.zycraft.network.SetFabricatorRecipePacket;

import javax.annotation.Nullable;
import java.util.Optional;

public class FabricatorRecipeTransferHandler implements IRecipeTransferHandler<FabricatorMenu, CraftingRecipe>
{
    private final IRecipeTransferHandlerHelper helper;

    public FabricatorRecipeTransferHandler(IRecipeTransferHandlerHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(FabricatorMenu menu, CraftingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer)
    {
        if (!FabricatorBlockEntity.isRecipeCompatible(recipe))
            return helper.createUserErrorWithTooltip(ZYLang.FABRICATOR_RECIPE_INCOMPATIBLE);

        if (doTransfer)
            ZYCraft.CHANNEL.sendToServer(new SetFabricatorRecipePacket(menu.containerId, recipe, recipeSlots));
        return null;
    }

    @Override
    public Class<FabricatorMenu> getContainerClass()
    {
        return FabricatorMenu.class;
    }

    @Override
    public Optional<MenuType<FabricatorMenu>> getMenuType()
    {
        return Optional.of(ZYMenus.FABRICATOR.get());//TODO: Cache optional?
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType()
    {
        return RecipeTypes.CRAFTING;
    }
}
