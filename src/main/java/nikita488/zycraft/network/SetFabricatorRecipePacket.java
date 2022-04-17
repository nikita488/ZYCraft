package nikita488.zycraft.network;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.network.NetworkEvent;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.menu.FabricatorMenu;

import java.util.List;
import java.util.function.Supplier;

public class SetFabricatorRecipePacket
{
    private final int menuID;
    private final ResourceLocation recipeID;
    private final NonNullList<ItemStack> recipePattern = NonNullList.withSize(9, ItemStack.EMPTY);
    private ItemStack craftingResult = ItemStack.EMPTY;

    public SetFabricatorRecipePacket(int menuID, CraftingRecipe recipe, IRecipeSlotsView recipeSlots)
    {
        this.menuID = menuID;
        this.recipeID = recipe.getId();

        List<IRecipeSlotView> inputSlots = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT);
        List<IRecipeSlotView> outputSlots = recipeSlots.getSlotViews(RecipeIngredientRole.OUTPUT);

        for (int slot = 0; slot < 9; slot++)
        {
            IRecipeSlotView view = inputSlots.get(slot);

            if (view.isEmpty())
                continue;

            ItemStack stack = view.getDisplayedIngredient(VanillaTypes.ITEM_STACK).orElse(ItemStack.EMPTY);

            if (!stack.isEmpty())
                recipePattern.set(slot, stack);
        }

        this.craftingResult = outputSlots.stream()
                .findFirst()
                .flatMap(view -> view.getDisplayedIngredient(VanillaTypes.ITEM_STACK))
                .orElse(ItemStack.EMPTY);
    }

    public SetFabricatorRecipePacket(FriendlyByteBuf buffer)
    {
        this.menuID = buffer.readVarInt();
        this.recipeID = buffer.readResourceLocation();

        for (int slot = 0; slot < 9; slot++)
            recipePattern.set(slot, buffer.readItem());

        this.craftingResult = buffer.readItem();
    }

    public static SetFabricatorRecipePacket decode(FriendlyByteBuf buffer)
    {
        return new SetFabricatorRecipePacket(buffer);
    }

    public static void encode(SetFabricatorRecipePacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(packet.menuID());
        buffer.writeResourceLocation(packet.recipeID());

        for (int slot = 0; slot < 9; slot++)
            buffer.writeItem(packet.recipePattern().get(slot));

        buffer.writeItem(packet.craftingResult());
    }

    public static boolean handle(SetFabricatorRecipePacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayer player = context.get().getSender();

            if (player == null)
                return;

            player.resetLastActionTime();

            AbstractContainerMenu menu = player.containerMenu;

            if (menu.containerId == packet.menuID() && menu instanceof FabricatorMenu fabricatorMenu && !player.isSpectator())
            {
                FabricatorBlockEntity fabricator = fabricatorMenu.blockEntity();

                if (fabricator == null)
                    return;

                for (int slot = 0; slot < 9; slot++)
                    fabricator.recipePattern().setItem(slot, packet.recipePattern().get(slot));

                CraftingRecipe recipe = (CraftingRecipe)player.getLevel().getRecipeManager().byKey(packet.recipeID()).orElse(null);

                fabricator.setCraftingRecipeAndResult(recipe, packet.craftingResult());
            }
        });

        return true;
    }

    public int menuID()
    {
        return menuID;
    }

    public ResourceLocation recipeID()
    {
        return recipeID;
    }

    public NonNullList<ItemStack> recipePattern()
    {
        return recipePattern;
    }

    public ItemStack craftingResult()
    {
        return craftingResult;
    }
}
