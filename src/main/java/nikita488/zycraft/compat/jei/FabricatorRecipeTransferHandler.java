package nikita488.zycraft.compat.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.ICraftingRecipe;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.menu.FabricatorContainer;
import nikita488.zycraft.network.SetFabricatorRecipePacket;
import nikita488.zycraft.tile.FabricatorTile;

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
        if (recipe instanceof ICraftingRecipe)
        {
            ICraftingRecipe craftingRecipe = (ICraftingRecipe)recipe;

            if (!FabricatorTile.isRecipeCompatible(craftingRecipe))
                return helper.createUserErrorWithTooltip(ZYLang.FABRICATOR_RECIPE_INCOMPATIBLE);

            if (doTransfer)
                ZYCraft.CHANNEL.sendToServer(new SetFabricatorRecipePacket(container.containerId, craftingRecipe, recipeLayout.getItemStacks().getGuiIngredients()));

            return null;
        }

        return helper.createInternalError();
    }

    @Override
    public Class<FabricatorContainer> getContainerClass()
    {
        return FabricatorContainer.class;
    }
}
