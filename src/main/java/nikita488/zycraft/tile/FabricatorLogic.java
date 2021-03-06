package nikita488.zycraft.tile;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import nikita488.zycraft.inventory.container.ZYContainer;
import nikita488.zycraft.util.ItemStackUtils;

import java.util.EnumMap;
import java.util.EnumSet;

public class FabricatorLogic
{
    private static final Direction[] SIDES = Direction.values();
    private final FabricatorTile fabricator;
    private final EnumSet<Direction> checkedSides = EnumSet.noneOf(Direction.class);
    private final EnumSet<Direction> pendingSides = EnumSet.noneOf(Direction.class);
    private final EnumMap<Direction, ObjectList<ItemStack>> pendingItems = new EnumMap<>(Direction.class);
    private final CraftingInventory craftingInventory = new CraftingInventory(ZYContainer.EMPTY_CONTAINER, 3, 3);
    private int foundIngredientCount;

    public FabricatorLogic(FabricatorTile fabricator)
    {
        this.fabricator = fabricator;
    }

    private LazyOptional<IItemHandler> getAdjacentInventory(IBlockReader world, BlockPos pos, Direction side)
    {
        TileEntity tile = world.getTileEntity(pos.offset(side));
        return tile != null ? tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()) : LazyOptional.empty();
    }

    private ItemStack insertItem(IItemHandler inventory, ItemStack stack)
    {
        return ItemHandlerHelper.insertItemStacked(inventory, stack, false);
    }

    public boolean updatePendingItems()
    {
        if (pendingItems.isEmpty())
            return false;

        for (Direction side : SIDES)
        {
            if (isSideChecked(side))
                continue;

            ObjectList<ItemStack> items = pendingItems.get(side);

            if (items == null)
                continue;

            setSideChecked(side);

            IItemHandler inventory = fabricator.inventory();

            if (side != Direction.UP)
            {
                LazyOptional<IItemHandler> capability = getAdjacentInventory(fabricator.getWorld(), fabricator.getPos(), side);

                if (!capability.isPresent())
                    continue;
                inventory = capability.orElse(EmptyHandler.INSTANCE);
            }

            for (ObjectListIterator<ItemStack> iterator = items.iterator(); iterator.hasNext();)
            {
                ItemStack remainder = insertItem(inventory, iterator.next());

                if (remainder.isEmpty())
                    iterator.remove();
                else
                    iterator.set(remainder);
            }

            if (items.isEmpty())
                pendingItems.remove(side);

            if (pendingItems.isEmpty())
                return false;
        }

        return true;
    }

    private void tryInsertItem(IItemHandler inventory, ItemStack stack)
    {
        tryInsertItem(Direction.UP, inventory, stack);
    }

    private void tryInsertItem(RecipeIngredient ingredient, ItemStack stack)
    {
        tryInsertItem(ingredient.side(), ingredient.inventory(), stack);
    }

    private void tryInsertItem(Direction side, IItemHandler inventory, ItemStack stack)
    {
        ItemStack remainder = insertItem(inventory, stack);

        if (remainder.isEmpty())
            return;

        addPendingItem(side, remainder);
        checkedSides.removeAll(pendingSides);
    }

    public void tryCraft()
    {
        World world = fabricator.getWorld();
        BlockPos pos = fabricator.getPos();
        ICraftingRecipe recipe = fabricator.recipe();
        ItemStack recipeResult = fabricator.craftingResult();

        if (recipe == null || recipeResult.isEmpty() || isAllSidesChecked())
            return;

        RecipeIngredient[] ingredients = getIngredients(recipe.getIngredients());

        if (ingredients == null)
            return;

        for (int slot = 0; slot < ingredients.length; slot++)
            craftingInventory.setInventorySlotContents(slot, ingredients[slot].stack());

        FakePlayer player = fabricator.player();

        player.setRawPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        recipeResult.onCrafting(world, player, recipeResult.getCount());
        BasicEventHooks.firePlayerCraftingEvent(player, recipeResult, craftingInventory);

        ForgeHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(craftingInventory);
        ForgeHooks.setCraftingPlayer(null);

        for (RecipeIngredient ingredient : ingredients)
            ingredient.extract();

        tryInsertItem(fabricator.inventory(), recipeResult);

        PlayerInventory playerInventory = player.inventory;

        for (int slot = 0; slot < playerInventory.getSizeInventory(); slot++)
        {
            ItemStack stack = playerInventory.getStackInSlot(slot);

            if (!stack.isEmpty())
                tryInsertItem(fabricator.inventory(), stack);
        }

        for (int slot = 0; slot < ingredients.length; slot++)
        {
            ItemStack stack = remainingItems.get(slot);

            if (!stack.isEmpty())
                tryInsertItem(ingredients[slot], stack);
        }

        playerInventory.clear();
        pendingSides.clear();
        craftingInventory.clear();
    }

    private RecipeIngredient[] getIngredients(NonNullList<Ingredient> recipeIngredients)
    {
        RecipeIngredient[] foundIngredients = new RecipeIngredient[recipeIngredients.size()];

        this.foundIngredientCount = 0;

        for (int i = 0; i < foundIngredients.length; i++)
        {
            if (recipeIngredients.get(i) != Ingredient.EMPTY)
                continue;
            foundIngredients[i] = RecipeIngredient.EMPTY;
            this.foundIngredientCount++;
        }

        if (!isSideChecked(Direction.UP))
        {
            setSideChecked(Direction.UP);

            if (tryFindIngredients(fabricator.inventory(), recipeIngredients, Direction.UP, foundIngredients))
                return foundIngredients;
        }

        for (Direction side : SIDES)
        {
            if (side == Direction.UP || isSideChecked(side))
                continue;

            setSideChecked(side);

            LazyOptional<IItemHandler> capability = getAdjacentInventory(fabricator.getWorld(), fabricator.getPos(), side);

            if (capability.isPresent() && tryFindIngredients(capability.orElse(EmptyHandler.INSTANCE), recipeIngredients, side, foundIngredients))
                return foundIngredients;
        }

        return null;
    }

    private boolean tryFindIngredients(IItemHandler inventory, NonNullList<Ingredient> recipeIngredients, Direction side, RecipeIngredient[] foundIngredients)
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = inventory.extractItem(slot, Integer.MAX_VALUE, true);

            if (stack.isEmpty())
                continue;

            for (int i = 0; i < foundIngredients.length; i++)
            {
                if (foundIngredients[i] != null || !recipeIngredients.get(i).test(stack))
                    continue;

                foundIngredients[i] = new RecipeIngredient(side, inventory, slot);
                this.foundIngredientCount++;
                pendingSides.add(side);

                stack.shrink(1);

                if (stack.isEmpty())
                    break;
            }

            if (foundIngredientCount == recipeIngredients.size())
                return true;
        }

        return false;
    }

    public void dropPendingItems()
    {
        BlockPos pos = fabricator.getPos();

        for (ObjectList<ItemStack> items : pendingItems.values())
            items.forEach(stack -> InventoryHelper.spawnItemStack(fabricator.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    public void load(CompoundNBT tag)
    {
        if (!tag.contains("PendingItems", Constants.NBT.TAG_LIST))
            return;

        ListNBT pendingItemTags = tag.getList("PendingItems", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < pendingItemTags.size(); i++)
        {
            CompoundNBT pendingItemTag = pendingItemTags.getCompound(i);
            Direction side = SIDES[pendingItemTag.getInt("Side")];

            addPendingItem(side, ItemStack.read(pendingItemTag));
        }
    }

    public void save(CompoundNBT tag)
    {
        ListNBT pendingItemTags = new ListNBT();

        pendingItems.forEach((side, items) ->
        {
            for (ItemStack stack : items)
            {
                CompoundNBT pendingItemTag = new CompoundNBT();

                pendingItemTag.putInt("Side", side.getIndex());
                stack.write(pendingItemTag);
                pendingItemTags.add(pendingItemTag);
            }
        });

        if (!pendingItemTags.isEmpty())
            tag.put("PendingItems", pendingItemTags);
    }

    private void setSideChecked(Direction side)
    {
        checkedSides.add(side);
    }

    public void setSideChanged(Direction side)
    {
        checkedSides.remove(side);
        checkedSides.removeAll(pendingSides);
        pendingSides.clear();
    }

    private boolean isSideChecked(Direction side)
    {
        return checkedSides.contains(side);
    }

    private boolean isAllSidesChecked()
    {
        return checkedSides.size() == SIDES.length;
    }

    public void recheckSides()
    {
        checkedSides.clear();
        pendingSides.clear();
    }

    private void addPendingItem(Direction side, ItemStack stack)
    {
        pendingItems.computeIfAbsent(side, key -> new ObjectArrayList<>()).add(stack);
    }

    private static class RecipeIngredient
    {
        private static final RecipeIngredient EMPTY = new RecipeIngredient(Direction.UP, EmptyHandler.INSTANCE, -1);
        private final Direction side;
        private final IItemHandler inventory;
        private final int slot;

        public RecipeIngredient(Direction side, IItemHandler inventory, int slot)
        {
            this.side = side;
            this.inventory = inventory;
            this.slot = slot;
        }

        private boolean isEmpty()
        {
            return this == EMPTY || inventory == EmptyHandler.INSTANCE || slot < 0;
        }

        private ItemStack extract()
        {
            return inventory.extractItem(slot, 1, false);
        }

        private ItemStack stack()
        {
            return ItemStackUtils.copy(inventory.getStackInSlot(slot), 1);
        }

        private Direction side()
        {
            return side;
        }

        private IItemHandler inventory()
        {
            return inventory;
        }
    }
}
