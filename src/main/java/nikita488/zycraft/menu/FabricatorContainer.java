package nikita488.zycraft.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.menu.data.IntMenuData;
import nikita488.zycraft.menu.slot.RecipePatternSlot;
import nikita488.zycraft.menu.slot.ZYSlot;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.util.ItemStackUtils;

import javax.annotation.Nullable;
import java.util.Optional;

public class FabricatorContainer extends ZYTileContainer<FabricatorTile>
{
    private final PlayerEntity player;
    private final World world;
    private final IntMenuData modeData;

    public FabricatorContainer(@Nullable ContainerType<?> type, int windowID, PlayerInventory playerInventory)
    {
        this(type, windowID, playerInventory, null, new Inventory(9), new ItemStackHandler(), new ItemStackHandler(9), new IntMenuData());
    }

    public FabricatorContainer(int windowID, PlayerInventory playerInventory, FabricatorTile fabricator)
    {
        this(ZYContainers.FABRICATOR.get(), windowID, playerInventory, fabricator, fabricator.recipePattern(), fabricator.craftingResult(), fabricator.inventory(), new IntMenuData(() -> fabricator.mode().ordinal()));
    }

    public FabricatorContainer(@Nullable ContainerType<?> type, int windowID, PlayerInventory playerInventory, @Nullable FabricatorTile fabricator, IInventory recipePattern, IItemHandler craftingResult, IItemHandler inventory, IntMenuData modeData)
    {
        super(type, windowID, fabricator);

        this.player = playerInventory.player;
        this.world = player.level;
        this.modeData = modeData;

        addVariable(modeData);
        checkContainerSize(recipePattern, 9);
        assertInventorySize(craftingResult, 1);
        assertInventorySize(inventory, 9);
        assertDataSize(data, 1);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                addSlot(new RecipePatternSlot(this, recipePattern, j + i * 3, 16 + j * 18, 25 + i * 18));

        addSlot(new ZYSlot(craftingResult, 0, 88, 43)
        {
            @Override
            public boolean mayPickup(PlayerEntity player)
            {
                return false;
            }

            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false;
            }
        });

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                addSlot(new ZYSlot(inventory, j + i * 3, 124 + j * 18, 25 + i * 18));

        addPlayerInventorySlots(playerInventory);
    }

    @Override
    protected ItemStack doClick(int slotIndex, int button, ClickType type, PlayerEntity player)
    {
        if (slotIndex >= 0 && slotIndex < 9)
        {
            if (type == ClickType.PICKUP || type == ClickType.QUICK_CRAFT)
                getSlot(slotIndex).set(ItemStackUtils.copy(player.inventory.getCarried(), 1));
            return ItemStack.EMPTY;
        }
        else if (slotIndex == 9)
        {
            if (tile == null || type != ClickType.PICKUP)
                return ItemStack.EMPTY;

            if (button == 0)
            {
                tile.logic().recheckSides();
                tile.logic().tryCraft();
            }
            else if (button == 1)
            {
                tile.recipePattern().clearContent();
                tile.setCraftingRecipeAndResult(null, ItemStack.EMPTY);
            }

            broadcastChanges();
            return ItemStack.EMPTY;
        }

        return super.doClick(slotIndex, button, type, player);
    }

    @Override
    public void slotsChanged(IInventory inventory)
    {
        if (tile == null)
            return;

        CraftingInventory recipePattern = tile.recipePattern();
        ICraftingRecipe lastRecipe = tile.craftingRecipe();

        if (lastRecipe != null && lastRecipe.matches(recipePattern, world))
            return;

        ServerPlayerEntity player = (ServerPlayerEntity)this.player;
        Optional<ICraftingRecipe> craftingRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, recipePattern, world)
                .filter(FabricatorTile::isRecipeCompatible);
        ItemStack craftingResult = craftingRecipe.map(recipe -> recipe.assemble(recipePattern)).orElse(ItemStack.EMPTY);

        tile.setCraftingRecipeAndResult(craftingRecipe.orElse(null), craftingResult);
        player.connection.send(new SSetSlotPacket(containerId, 9, craftingResult));
    }

    @Override
    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return slotIndex >= 10 && slotIndex < 19 ? moveItemStackTo(stack, 19, 55, false) : moveItemStackTo(stack, 10, 19, false);
    }

    @Override
    public boolean clickMenuButton(PlayerEntity player, int index)
    {
        if (tile != null)
            tile.setMode(FabricatorMode.VALUES[index]);
        return modeData.getAsInt() != index;
    }

    public IntMenuData modeData()
    {
        return modeData;
    }
}
