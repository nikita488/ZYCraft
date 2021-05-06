package nikita488.zycraft.compat.jei.transfer;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.ICraftingRecipe;
import nikita488.zycraft.inventory.container.FabricatorContainer;
import nikita488.zycraft.network.SetRecipePatternPacket;
import nikita488.zycraft.network.ZYChannel;

import javax.annotation.Nullable;

public class FabricatorRecipeTransferHandler implements IRecipeTransferHandler<FabricatorContainer>
{
    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(FabricatorContainer container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer)
    {
        if (doTransfer && recipe instanceof ICraftingRecipe)
            ZYChannel.INSTANCE.sendToServer(new SetRecipePatternPacket(container.windowId, (ICraftingRecipe)recipe, recipeLayout.getItemStacks().getGuiIngredients()));
        return null;
    }

    @Override
    public Class<FabricatorContainer> getContainerClass()
    {
        return FabricatorContainer.class;
    }
}
