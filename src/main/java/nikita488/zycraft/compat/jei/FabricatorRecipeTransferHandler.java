package nikita488.zycraft.compat.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.ICraftingRecipe;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.inventory.container.FabricatorContainer;
import nikita488.zycraft.network.SetFabricatorRecipePacket;

import javax.annotation.Nullable;

public class FabricatorRecipeTransferHandler implements IRecipeTransferHandler<FabricatorContainer>
{
    private final IRecipeTransferHandlerHelper helper;

    public FabricatorRecipeTransferHandler(IRecipeTransferHandlerHelper helper)
    {
        this.helper = helper;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(FabricatorContainer container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer)
    {
        if (!(recipe instanceof ICraftingRecipe))
            return helper.createInternalError();

        ICraftingRecipe craftingRecipe = (ICraftingRecipe)recipe;

        if (craftingRecipe.isDynamic() || !craftingRecipe.canFit(3, 3) || craftingRecipe.getIngredients().isEmpty() || craftingRecipe.getRecipeOutput().isEmpty())
            return helper.createUserErrorWithTooltip(ZYLang.RECIPE_INCOMPATIBLE);

        if (doTransfer)
            ZYCraft.CHANNEL.sendToServer(new SetFabricatorRecipePacket(container.windowId, craftingRecipe, recipeLayout.getItemStacks().getGuiIngredients()));
        return null;
    }

    @Override
    public Class<FabricatorContainer> getContainerClass()
    {
        return FabricatorContainer.class;
    }
}
