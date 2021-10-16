package nikita488.zycraft.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
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
import nikita488.zycraft.util.SerializableCraftingInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class FabricatorTile extends ZYTile implements ITickableTileEntity, INamedContainerProvider
{
    private static final GameProfile PROFILE = new GameProfile(UUID.randomUUID(), "[Fabricator]");
    private final FabricatorLogic logic = new FabricatorLogic(this);
    private final SerializableCraftingInventory recipePattern = new SerializableCraftingInventory(ZYContainer.EMPTY_CONTAINER, 3, 3)
    {
        @Override
        public void setChanged()
        {
            FabricatorTile.this.setChanged();
        }
    };
    @Nullable
    private ICraftingRecipe craftingRecipe;
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
    private final NonNullLazy<FakePlayer> player = NonNullLazy.of(() -> FakePlayerFactory.get((ServerWorld)level, PROFILE));
    private final Supplier<FabricatorMode> mode = () -> getBlockState().getValue(FabricatorBlock.MODE);
    private int reloadCount = DataPackReloadCounter.INSTANCE.count();
    @Nullable
    private ResourceLocation pendingRecipe;
    private boolean lastPowered;

    public FabricatorTile(TileEntityType<?> type)
    {
        super(type);
    }

    public static boolean isRecipeCompatible(IRecipe<?> recipe)
    {
        return recipe.getType() == IRecipeType.CRAFTING && !recipe.isSpecial() && recipe.canCraftInDimensions(3, 3) && !recipe.getIngredients().isEmpty() && !recipe.getResultItem().isEmpty();
    }

    @Override
    public void tick()
    {
        if (level == null || level.isClientSide())
            return;

        if (reloadCount != DataPackReloadCounter.INSTANCE.count())
        {
            if (craftingRecipe != null)
            {
                this.pendingRecipe = craftingRecipe.getId();
                setCraftingRecipeAndResult(null, ItemStack.EMPTY);
            }

            this.reloadCount = DataPackReloadCounter.INSTANCE.count();
        }

        if (pendingRecipe != null)
        {
            Optional<ICraftingRecipe> craftingRecipe = level.getRecipeManager().byKey(pendingRecipe)
                    .filter(FabricatorTile::isRecipeCompatible)
                    .flatMap(recipe -> IRecipeType.CRAFTING.tryMatch((ICraftingRecipe)recipe, level, recipePattern));

            if (!craftingRecipe.isPresent())
                craftingRecipe = level.getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, recipePattern, level)
                        .filter(FabricatorTile::isRecipeCompatible);

            ItemStack craftingResult = craftingRecipe.map(recipe -> recipe.assemble(recipePattern)).orElse(ItemStack.EMPTY);

            setCraftingRecipeAndResult(craftingRecipe.orElse(null), craftingResult);
            this.pendingRecipe = null;
        }

        if (logic.updatePendingItems())
            return;

        boolean powered = level.hasNeighborSignal(worldPosition);

        if (mode.get().canCraft(lastPowered, powered))
            logic.tryCraft();

        if (powered != lastPowered)
        {
            this.lastPowered = powered;
            setChanged();
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
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);

        this.lastPowered = tag.getBoolean("LastPowered");

        if (tag.contains("Recipe", Constants.NBT.TAG_STRING))
            this.pendingRecipe = new ResourceLocation(tag.getString("Recipe"));

        recipePattern.load(tag.getCompound("RecipePattern"));
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        logic.load(tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);

        if (craftingRecipe != null)
            tag.putString("Recipe", craftingRecipe.getId().toString());

        tag.put("RecipePattern", recipePattern.save(new CompoundNBT()));
        tag.put("Inventory", inventory.serializeNBT());
        tag.putBoolean("LastPowered", lastPowered);
        logic.save(tag);

        return tag;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return ZYLang.FABRICATOR;
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player)
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

    public CraftingInventory recipePattern()
    {
        return recipePattern;
    }

    @Nullable
    public ICraftingRecipe craftingRecipe()
    {
        return craftingRecipe;
    }

    public void setCraftingRecipeAndResult(@Nullable ICraftingRecipe recipe, ItemStack result)
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
