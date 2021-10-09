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

    public SetFabricatorRecipePacket(PacketBuffer buf)
    {
        this.windowID = buf.readVarInt();
        this.recipeID = buf.readResourceLocation();

        for (int slot = 0; slot < 9; slot++)
            recipePattern.set(slot, buf.readItemStack());

        this.craftingResult = buf.readItemStack();
    }

    public static SetFabricatorRecipePacket decode(PacketBuffer buf)
    {
        return new SetFabricatorRecipePacket(buf);
    }

    public static void encode(SetFabricatorRecipePacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeResourceLocation(msg.recipeID());

        for (int slot = 0; slot < 9; slot++)
            buf.writeItemStack(msg.recipePattern().get(slot));

        buf.writeItemStack(msg.craftingResult());
    }

    public static boolean handle(SetFabricatorRecipePacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = ctx.get().getSender();

            if (player == null)
                return;

            player.markPlayerActive();

            Container container = player.openContainer;

            if (container.windowId == msg.windowID() && container.getCanCraft(player) && container instanceof FabricatorContainer && !player.isSpectator())
            {
                FabricatorTile fabricator = ((FabricatorContainer)container).tile();

                if (fabricator == null)
                    return;

                for (int slot = 0; slot < 9; slot++)
                    fabricator.recipePattern().setInventorySlotContents(slot, msg.recipePattern().get(slot));

                ICraftingRecipe recipe = (ICraftingRecipe)player.getServerWorld().getRecipeManager().getRecipe(msg.recipeID()).orElse(null);

                fabricator.setCraftingRecipeAndResult(recipe, msg.craftingResult());
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
