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
import nikita488.zycraft.inventory.container.FabricatorContainer;
import nikita488.zycraft.tile.FabricatorTile;

import java.util.Map;
import java.util.function.Supplier;

public class SetRecipePatternPacket
{
    private final int windowID;
    private final ResourceLocation recipeID;
    private final NonNullList<ItemStack> recipePattern = NonNullList.withSize(9, ItemStack.EMPTY);
    private ItemStack recipeResult = ItemStack.EMPTY;

    public SetRecipePatternPacket(int windowID, ICraftingRecipe recipe, Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients)
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
                this.recipeResult = stack;
        });
    }

    public SetRecipePatternPacket(PacketBuffer buf)
    {
        this.windowID = buf.readVarInt();
        this.recipeID = buf.readResourceLocation();

        for (int i = 0; i < 9; i++)
            recipePattern.set(i, buf.readItemStack());

        this.recipeResult = buf.readItemStack();
    }

    public static SetRecipePatternPacket decode(PacketBuffer buf)
    {
        return new SetRecipePatternPacket(buf);
    }

    public static void encode(SetRecipePatternPacket msg, PacketBuffer buf)
    {
        buf.writeVarInt(msg.windowID());
        buf.writeResourceLocation(msg.recipeID());

        for (int i = 0; i < 9; i++)
            buf.writeItemStack(msg.recipePattern().get(i));

        buf.writeItemStack(msg.recipeResult());
    }

    public static boolean handle(SetRecipePatternPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = ctx.get().getSender();

            if (player == null)
                return;

            player.markPlayerActive();

            Container container = player.openContainer;

            if (container.windowId != msg.windowID() || !container.getCanCraft(player) || !(container instanceof FabricatorContainer) || player.isSpectator())
                return;

            FabricatorTile fabricator = ((FabricatorContainer)container).tile();

            if (fabricator == null)
                return;

            for (int i = 0; i < 9; i++)
                fabricator.recipePattern().setInventorySlotContents(i, msg.recipePattern().get(i));

            fabricator.setRecipe((ICraftingRecipe)player.getServerWorld().getRecipeManager().getRecipe(msg.recipeID()).orElse(null));
            fabricator.setCraftingResult(msg.recipeResult());
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

    public ItemStack recipeResult()
    {
        return recipeResult;
    }
}
