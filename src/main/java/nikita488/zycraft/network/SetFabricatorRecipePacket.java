package nikita488.zycraft.network;

import mezz.jei.api.gui.ingredient.IGuiIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.menu.FabricatorMenu;

import java.util.Map;
import java.util.function.Supplier;

public class SetFabricatorRecipePacket
{
    private final int windowID;
    private final ResourceLocation recipeID;
    private final NonNullList<ItemStack> recipePattern = NonNullList.withSize(9, ItemStack.EMPTY);
    private ItemStack craftingResult = ItemStack.EMPTY;

    public SetFabricatorRecipePacket(int windowID, CraftingRecipe recipe, Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients)
    {
        this.windowID = windowID;
        this.recipeID = recipe.getId();

        ingredients.forEach((index, ingredient) ->
        {
            ItemStack stack = ingredient.getDisplayedIngredient();

            if (stack == null)
                return;

            if (ingredient.isInput())
                recipePattern.set(index - 1, stack);
            else
                this.craftingResult = stack;
        });
    }

    public SetFabricatorRecipePacket(FriendlyByteBuf buffer)
    {
        this.windowID = buffer.readVarInt();
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
        buffer.writeVarInt(packet.windowID());
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

            if (menu.containerId == packet.windowID() && menu instanceof FabricatorMenu fabricatorMenu && !player.isSpectator())
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

    public int windowID()
    {
        return windowID;
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
