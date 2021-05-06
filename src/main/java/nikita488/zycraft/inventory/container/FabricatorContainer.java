package nikita488.zycraft.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.inventory.container.slot.RecipePatternSlot;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.util.ItemStackUtils;

import javax.annotation.Nullable;
import java.util.Optional;

public class FabricatorContainer extends ZYTileContainer<FabricatorTile>
{
    private final PlayerEntity player;
    private final World world;

    public FabricatorContainer(@Nullable ContainerType<?> type, int windowID, PlayerInventory playerInventory)
    {
        this(type, windowID, playerInventory, null, new Inventory(9), new Inventory(1), new ItemStackHandler(9));
    }

    public FabricatorContainer(int windowID, PlayerInventory playerInventory, FabricatorTile fabricator)
    {
        this(ZYContainers.FABRICATOR.get(), windowID, playerInventory, fabricator, fabricator.recipePattern(), fabricator.recipeResult(), fabricator.inventory());
    }

    public FabricatorContainer(@Nullable ContainerType<?> type, int windowID, PlayerInventory playerInventory, @Nullable FabricatorTile fabricator, IInventory recipePattern, IInventory recipeResult, IItemHandler inventory)
    {
        super(type, windowID, fabricator);

        this.player = playerInventory.player;
        this.world = player.world;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                addSlot(new RecipePatternSlot(this, recipePattern, j + i * 3, 16 + j * 18, 25 + i * 18));

        addSlot(new Slot(recipeResult, 0, 88, 43)
        {
            @Override
            public boolean canTakeStack(PlayerEntity player)
            {
                return false;
            }

            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                addSlot(new SlotItemHandler(inventory, j + i * 3, 124 + j * 18, 25 + i * 18));

        addPlayerInventorySlots(playerInventory);
    }

    @Override
    protected ItemStack func_241440_b_(int slotIndex, int button, ClickType type, PlayerEntity player)
    {
        if (slotIndex >= 0 && slotIndex < 9)
        {
            if (type == ClickType.PICKUP || type == ClickType.QUICK_CRAFT)
                getSlot(slotIndex).putStack(ItemStackUtils.copy(player.inventory.getItemStack(), 1));
            return ItemStack.EMPTY;
        }
        else if (slotIndex == 9)
        {
            if (tile == null || type != ClickType.PICKUP)
                return ItemStack.EMPTY;

            if (button == 0)
            {
                System.out.println("Request craft");
            }
            else if (button == 1)
            {
                tile.setRecipe(null);
                tile.recipePattern().clear();
                tile.recipeResult().clear();
                detectAndSendChanges();
            }

            return ItemStack.EMPTY;
        }

        return super.func_241440_b_(slotIndex, button, type, player);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory)
    {
        //TODO: Reset checked inventories when craft result is changed
        if (tile == null)
            return;

        CraftingInventory recipePattern = tile.recipePattern();
        ICraftingRecipe lastRecipe = tile.recipe();

        if (lastRecipe != null && lastRecipe.matches(recipePattern, world))
            return;

        ServerPlayerEntity player = (ServerPlayerEntity)this.player;
        Optional<ICraftingRecipe> recipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, recipePattern, world)
                .filter(craftingRecipe -> tile.recipeResult().canUseRecipe(world, player, craftingRecipe));
        ItemStack result = recipe.map(craftingRecipe -> craftingRecipe.getCraftingResult(recipePattern))
                .orElse(ItemStack.EMPTY);

        tile.setRecipe(recipe.orElse(null));
        tile.setCraftingResult(result);
        player.connection.sendPacket(new SSetSlotPacket(windowId, 9, result));
    }

    @Override
    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return slotIndex >= 10 && slotIndex < 19 ? mergeItemStack(stack, 19, 55, false) : mergeItemStack(stack, 10, 19, false);
    }
}
