package nikita488.zycraft.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.menu.FabricatorContainer;
import nikita488.zycraft.menu.ZYContainer;
import nikita488.zycraft.util.DataPackReloadCounter;
import nikita488.zycraft.util.InventoryUtils;
import nikita488.zycraft.util.SerializableCraftingContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class FabricatorTile extends ZYTile implements MenuProvider
{
    private static final GameProfile PROFILE = new GameProfile(UUID.randomUUID(), "[Fabricator]");
    private final FabricatorLogic logic = new FabricatorLogic(this);
    private final SerializableCraftingContainer recipePattern = new SerializableCraftingContainer(ZYContainer.EMPTY_CONTAINER, 3, 3)
    {
        @Override
        public void setChanged()
        {
            FabricatorTile.this.setChanged();
        }
    };
    @Nullable
    private CraftingRecipe craftingRecipe;
    private final ItemStackHandler craftingResult = new ItemStackHandler();
    private final ItemStackHandler inventory = new ItemStackHandler(9)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
            logic.setSideChanged(Direction.UP);
        }
    };
    private final LazyOptional<IItemHandler> capability = LazyOptional.of(() -> inventory);
    private final NonNullLazy<FakePlayer> player = NonNullLazy.of(() -> FakePlayerFactory.get((ServerLevel)level, PROFILE));
    private final Supplier<FabricatorMode> mode = () -> getBlockState().getValue(FabricatorBlock.MODE);
    private int reloadCount = DataPackReloadCounter.INSTANCE.count();
    @Nullable
    private ResourceLocation pendingRecipe;
    private boolean lastPowered;

    public FabricatorTile(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public static boolean isRecipeCompatible(Recipe<?> recipe)
    {
        return recipe.getType() == RecipeType.CRAFTING && !recipe.isSpecial() && recipe.canCraftInDimensions(3, 3) && !recipe.getIngredients().isEmpty() && !recipe.getResultItem().isEmpty();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FabricatorTile fabricator)
    {
        if (fabricator.reloadCount != DataPackReloadCounter.INSTANCE.count())
        {
            if (fabricator.craftingRecipe != null)
            {
                fabricator.pendingRecipe = fabricator.craftingRecipe.getId();
                fabricator.setCraftingRecipeAndResult(null, ItemStack.EMPTY);
            }

            fabricator.reloadCount = DataPackReloadCounter.INSTANCE.count();
        }

        if (fabricator.pendingRecipe != null)
        {
            Optional<CraftingRecipe> craftingRecipe = level.getRecipeManager().byKey(fabricator.pendingRecipe)
                    .filter(FabricatorTile::isRecipeCompatible)
                    .flatMap(recipe -> RecipeType.CRAFTING.tryMatch((CraftingRecipe)recipe, level, fabricator.recipePattern));

            if (craftingRecipe.isEmpty())
                craftingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, fabricator.recipePattern, level)
                        .filter(FabricatorTile::isRecipeCompatible);

            ItemStack craftingResult = craftingRecipe.map(recipe -> recipe.assemble(fabricator.recipePattern)).orElse(ItemStack.EMPTY);

            fabricator.setCraftingRecipeAndResult(craftingRecipe.orElse(null), craftingResult);
            fabricator.pendingRecipe = null;
        }

        if (fabricator.logic.updatePendingItems())
            return;

        boolean powered = level.hasNeighborSignal(pos);

        if (fabricator.mode.get().canCraft(fabricator.lastPowered, powered))
            fabricator.logic.tryCraft();

        if (powered != fabricator.lastPowered)
        {
            fabricator.lastPowered = powered;
            fabricator.setChanged();
        }
    }

    public int getColor(BlockState state)
    {
        return state.getValue(FabricatorBlock.MODE).rgb(level.hasNeighborSignal(worldPosition));
    }

    public void dropItems()
    {
        InventoryUtils.dropInventoryItems(level, worldPosition, inventory);
        logic.dropPendingItems();
    }

    public int getAnalogOutputSignal()
    {
        return ItemHandlerHelper.calcRedstoneFromInventory(inventory);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        this.lastPowered = tag.getBoolean("LastPowered");

        if (tag.contains("Recipe", Constants.NBT.TAG_STRING))
            this.pendingRecipe = new ResourceLocation(tag.getString("Recipe"));

        recipePattern.load(tag.getCompound("RecipePattern"));
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        logic.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        super.save(tag);

        if (craftingRecipe != null)
            tag.putString("Recipe", craftingRecipe.getId().toString());

        tag.put("RecipePattern", recipePattern.save(new CompoundTag()));
        tag.put("Inventory", inventory.serializeNBT());
        tag.putBoolean("LastPowered", lastPowered);
        logic.save(tag);

        return tag;
    }

    @Override
    public Component getDisplayName()
    {
        return ZYLang.FABRICATOR;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player player)
    {
        return new FabricatorContainer(windowID, playerInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nullable Direction side)
    {
        return !remove && side != Direction.UP && type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? capability.cast() : super.getCapability(type, side);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        capability.invalidate();
    }

    public FabricatorLogic logic()
    {
        return logic;
    }

    public CraftingContainer recipePattern()
    {
        return recipePattern;
    }

    @Nullable
    public CraftingRecipe craftingRecipe()
    {
        return craftingRecipe;
    }

    public void setCraftingRecipeAndResult(@Nullable CraftingRecipe recipe, ItemStack result)
    {
        this.craftingRecipe = recipe != null && result.isEmpty() ? null : recipe;
        craftingResult.setStackInSlot(0, result);
        logic.recheckSides();
        setChanged();
    }

    public ItemStackHandler craftingResult()
    {
        return craftingResult;
    }

    public ItemStack craftingResultStack()
    {
        return craftingResult.getStackInSlot(0).copy();
    }

    public ItemStackHandler inventory()
    {
        return inventory;
    }

    public FakePlayer player()
    {
        return player.get();
    }

    public FabricatorMode mode()
    {
        return mode.get();
    }

    public void setMode(FabricatorMode mode)
    {
        level.setBlock(worldPosition, getBlockState().setValue(FabricatorBlock.MODE, mode), Constants.BlockFlags.BLOCK_UPDATE);
    }
}
