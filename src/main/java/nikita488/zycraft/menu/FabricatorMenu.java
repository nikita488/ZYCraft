package nikita488.zycraft.menu;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.init.ZYMenus;
import nikita488.zycraft.menu.data.IntMenuData;
import nikita488.zycraft.menu.slot.RecipePatternSlot;
import nikita488.zycraft.menu.slot.ZYSlot;
import nikita488.zycraft.util.ItemStackUtils;

import javax.annotation.Nullable;
import java.util.Optional;

public class FabricatorMenu extends ZYBlockEntityMenu<FabricatorBlockEntity>
{
    private final Level level;
    private final Player player;
    private final IntMenuData modeData;

    public FabricatorMenu(@Nullable MenuType<?> type, int windowID, Inventory playerInventory)
    {
        this(type, windowID, playerInventory, null, new SimpleContainer(9), new ItemStackHandler(), new ItemStackHandler(9), new IntMenuData());
    }

    public FabricatorMenu(int windowID, Inventory playerInventory, FabricatorBlockEntity fabricator)
    {
        this(ZYMenus.FABRICATOR.get(), windowID, playerInventory, fabricator, fabricator.recipePattern(), fabricator.craftingResult(), fabricator.inventory(), new IntMenuData(() -> fabricator.mode().ordinal()));
    }

    public FabricatorMenu(@Nullable MenuType<?> type, int windowID, Inventory playerInventory, @Nullable FabricatorBlockEntity fabricator, Container recipePattern, IItemHandler craftingResult, IItemHandler inventory, IntMenuData modeData)
    {
        super(type, windowID, fabricator);

        this.player = playerInventory.player;
        this.level = player.level;
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
            public boolean mayPickup(Player player)
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
    protected void doClick(int slotIndex, int button, ClickType type, Player player)
    {
        if (slotIndex >= 0 && slotIndex < 9)
        {
            if (type == ClickType.PICKUP || type == ClickType.QUICK_CRAFT)
                getSlot(slotIndex).set(ItemStackUtils.copy(getCarried(), 1));
        }
        else if (slotIndex == 9)
        {
            if (blockEntity == null || type != ClickType.PICKUP)
                return;

            if (button == 0)
            {
                blockEntity.logic().recheckSides();
                blockEntity.logic().tryCraft();
            }
            else if (button == 1)
            {
                blockEntity.recipePattern().clearContent();
                blockEntity.setCraftingRecipeAndResult(null, ItemStack.EMPTY);
            }

            broadcastChanges();
        }
        else
        {
            super.doClick(slotIndex, button, type, player);
        }
    }

    @Override
    public void slotsChanged(Container inventory)
    {
        if (blockEntity == null)
            return;

        CraftingContainer recipePattern = blockEntity.recipePattern();
        CraftingRecipe lastRecipe = blockEntity.craftingRecipe();

        if (lastRecipe != null && lastRecipe.matches(recipePattern, level))
            return;

        ServerPlayer player = (ServerPlayer)this.player;
        Optional<CraftingRecipe> craftingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, recipePattern, level)
                .filter(FabricatorBlockEntity::isRecipeCompatible);
        ItemStack craftingResult = craftingRecipe.map(recipe -> recipe.assemble(recipePattern)).orElse(ItemStack.EMPTY);

        blockEntity.setCraftingRecipeAndResult(craftingRecipe.orElse(null), craftingResult);
        player.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 9, craftingResult));
    }

    @Override
    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return slotIndex >= 10 && slotIndex < 19 ? moveItemStackTo(stack, 19, 55, false) : moveItemStackTo(stack, 10, 19, false);
    }

    @Override
    public boolean clickMenuButton(Player player, int index)
    {
        if (blockEntity != null)
            blockEntity.setMode(FabricatorMode.VALUES[index]);
        return modeData.getAsInt() != index;
    }

    public IntMenuData modeData()
    {
        return modeData;
    }
}
