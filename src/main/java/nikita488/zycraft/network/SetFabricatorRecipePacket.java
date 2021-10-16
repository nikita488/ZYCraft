package nikita488.zycraft.network;

import mezz.jei.api.gui.ingredient.IGuiIngredient;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import nikita488.zycraft.menu.FabricatorContainer;
import nikita488.zycraft.tile.FabricatorTile;

import java.util.Map;
import java.util.function.Supplier;

public class SetFabricatorRecipePacket
{
    private final int windowID;
    private final ResourceLocation recipeID;
    private final NonNullList<ItemStack> recipePattern = NonNullList.withSize(9, ItemStack.EMPTY);
    private ItemStack craftingResult = ItemStack.EMPTY;

    public SetFabricatorRecipePacket(int windowID, ICraftingRecipe recipe, Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients)
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

    public SetFabricatorRecipePacket(PacketBuffer buffer)
    {
        this.windowID = buffer.readVarInt();
        this.recipeID = buffer.readResourceLocation();

        for (int slot = 0; slot < 9; slot++)
            recipePattern.set(slot, buffer.readItem());

        this.craftingResult = buffer.readItem();
    }

    public static SetFabricatorRecipePacket decode(PacketBuffer buffer)
    {
        return new SetFabricatorRecipePacket(buffer);
    }

    public static void encode(SetFabricatorRecipePacket packet, PacketBuffer buffer)
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
            ServerPlayerEntity player = context.get().getSender();

            if (player == null)
                return;

            player.resetLastActionTime();

            Container container = player.containerMenu;

            if (container.containerId == packet.windowID() && container.isSynched(player) && container instanceof FabricatorContainer && !player.isSpectator())
            {
                FabricatorTile fabricator = ((FabricatorContainer)container).blockEntity();

                if (fabricator == null)
                    return;

                for (int slot = 0; slot < 9; slot++)
                    fabricator.recipePattern().setItem(slot, packet.recipePattern().get(slot));

                ICraftingRecipe recipe = (ICraftingRecipe)player.getLevel().getRecipeManager().byKey(packet.recipeID()).orElse(null);

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
