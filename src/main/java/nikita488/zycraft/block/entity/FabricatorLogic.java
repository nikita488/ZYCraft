package nikita488.zycraft.block.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import nikita488.zycraft.menu.ZYMenu;
import nikita488.zycraft.util.ItemStackUtils;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.EnumSet;

public class FabricatorLogic
{
    private final FabricatorBlockEntity fabricator;
    private final EnumSet<Direction> checkedSides = EnumSet.noneOf(Direction.class);
    private final EnumSet<Direction> pendingSides = EnumSet.noneOf(Direction.class);
    private final EnumMap<Direction, ObjectList<ItemStack>> pendingItems = new EnumMap<>(Direction.class);
    private final CraftingContainer craftingInventory = new CraftingContainer(ZYMenu.EMPTY_MENU, 3, 3);
    private int foundIngredientCount;

    public FabricatorLogic(FabricatorBlockEntity fabricator)
    {
        this.fabricator = fabricator;
    }

    private LazyOptional<IItemHandler> getRelativeInventory(Direction side)
    {
        Level level = fabricator.getLevel();
        BlockPos relativePos = fabricator.getBlockPos().relative(side);

        if (!level.isLoaded(relativePos))
            return LazyOptional.empty();

        BlockEntity blockEntity = level.getBlockEntity(relativePos);
        return blockEntity != null ? blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()) : LazyOptional.empty();
    }

    private ItemStack insertItem(IItemHandler inventory, ItemStack stack)
    {
        return ItemHandlerHelper.insertItemStacked(inventory, stack, false);
    }

    public boolean updatePendingItems()
    {
        if (pendingItems.isEmpty())
            return false;

        for (Direction side : ZYConstants.DIRECTIONS)
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
                LazyOptional<IItemHandler> capability = getRelativeInventory(side);

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
            {
                pendingItems.remove(side);
                fabricator.setChanged();
            }

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
        fabricator.setChanged();
        checkedSides.removeAll(pendingSides);
    }

    public void tryCraft()
    {
        CraftingRecipe recipe = fabricator.craftingRecipe();
        ItemStack craftingResult = fabricator.craftingResultStack();

        if (recipe == null || craftingResult.isEmpty() || isAllSidesChecked())
            return;

        RecipeIngredient[] ingredients = getIngredients(recipe.getIngredients());

        if (ingredients == null)
            return;

        for (int slot = 0; slot < ingredients.length; slot++)
            craftingInventory.setItem(slot, ingredients[slot].stack());

        FakePlayer player = fabricator.player();
        BlockPos pos = fabricator.getBlockPos();

        player.setPosRaw(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        craftingResult.onCraftedBy(fabricator.getLevel(), player, craftingResult.getCount());
        ForgeEventFactory.firePlayerCraftingEvent(player, craftingResult, craftingInventory);

        ForgeHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(craftingInventory);
        ForgeHooks.setCraftingPlayer(null);

        for (RecipeIngredient ingredient : ingredients)
            ingredient.extract();

        tryInsertItem(fabricator.inventory(), craftingResult);

        Inventory playerInventory = player.getInventory();

        for (int slot = 0; slot < playerInventory.getContainerSize(); slot++)
        {
            ItemStack stack = playerInventory.getItem(slot);

            if (!stack.isEmpty())
                tryInsertItem(fabricator.inventory(), stack);
        }

        for (int slot = 0; slot < ingredients.length; slot++)
        {
            ItemStack stack = remainingItems.get(slot);

            if (!stack.isEmpty())
                tryInsertItem(ingredients[slot], stack);
        }

        pendingSides.clear();
        playerInventory.clearContent();
        craftingInventory.clearContent();
    }

    @Nullable
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

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            if (side == Direction.UP || isSideChecked(side))
                continue;

            setSideChecked(side);

            LazyOptional<IItemHandler> capability = getRelativeInventory(side);

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
        BlockPos pos = fabricator.getBlockPos();

        for (ObjectList<ItemStack> items : pendingItems.values())
            items.forEach(stack -> Containers.dropItemStack(fabricator.getLevel(), pos.getX(), pos.getY(), pos.getZ(), stack));
    }

    public void load(CompoundTag tag)
    {
        if (!tag.contains("PendingItems", Tag.TAG_LIST))
            return;

        ListTag pendingItemTags = tag.getList("PendingItems", Tag.TAG_COMPOUND);

        for (int i = 0; i < pendingItemTags.size(); i++)
        {
            CompoundTag pendingItemTag = pendingItemTags.getCompound(i);
            Direction side = ZYConstants.DIRECTIONS[pendingItemTag.getInt("Side")];

            addPendingItem(side, ItemStack.of(pendingItemTag));
        }
    }

    public void save(CompoundTag tag)
    {
        ListTag pendingItemTags = new ListTag();

        pendingItems.forEach((side, items) ->
        {
            for (ItemStack stack : items)
            {
                CompoundTag pendingItemTag = new CompoundTag();

                pendingItemTag.putInt("Side", side.get3DDataValue());
                stack.save(pendingItemTag);
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
        return checkedSides.size() == ZYConstants.DIRECTIONS.length;
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
            return this == EMPTY || inventory.getSlots() <= 0 || slot < 0;
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
