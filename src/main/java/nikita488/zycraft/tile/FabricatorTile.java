package nikita488.zycraft.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.inventory.container.FabricatorContainer;
import nikita488.zycraft.inventory.container.ZYContainer;
import nikita488.zycraft.util.DataPackReloadCounter;
import nikita488.zycraft.util.InventoryUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class FabricatorTile extends ZYTile implements ITickableTileEntity, INamedContainerProvider
{
    private static final GameProfile PROFILE = new GameProfile(UUID.randomUUID(), "[Fabricator]");
    private final FabricatorLogic logic = new FabricatorLogic(this);
    private final CraftingInventory recipePattern = new CraftingInventory(ZYContainer.EMPTY_CONTAINER, 3, 3);
    private final CraftResultInventory recipeResult = new CraftResultInventory()
    {
        @Override
        public boolean canUseRecipe(World world, ServerPlayerEntity player, IRecipe<?> recipe)
        {
            return recipe.getType() == IRecipeType.CRAFTING && !recipe.isDynamic() && recipe.canFit(3, 3) && !recipe.getIngredients().isEmpty() && !recipe.getRecipeOutput().isEmpty();
        }
    };
    private final ItemStackHandler inventory = new ItemStackHandler(9)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            markDirty();
            logic.setInventoryChanged(Direction.UP);
        }
    };
    private final LazyOptional<IItemHandler> capability = LazyOptional.of(() -> inventory);
    private int reloadCount = DataPackReloadCounter.INSTANCE.count();
    @Nullable
    private ResourceLocation pendingRecipe;
    private FakePlayer player;
    private boolean lastPowered;

    public FabricatorTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
        if (world == null || world.isRemote())
            return;

        if (reloadCount != DataPackReloadCounter.INSTANCE.count())
        {
            IRecipe<?> recipe = recipeResult.getRecipeUsed();

            if (recipe != null)
            {
                this.pendingRecipe = recipe.getId();

                setRecipe(null);
                recipeResult.clear();
            }

            this.reloadCount = DataPackReloadCounter.INSTANCE.count();
        }

        if (pendingRecipe != null)
        {
            Optional<ICraftingRecipe> craftingRecipe = world.getRecipeManager().getRecipe(pendingRecipe)
                    .filter(recipe -> recipeResult.canUseRecipe(world, getPlayer(), recipe))
                    .flatMap(recipe -> IRecipeType.CRAFTING.matches((ICraftingRecipe)recipe, world, recipePattern));

            if (!craftingRecipe.isPresent())
                craftingRecipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, recipePattern, world)
                        .filter(recipe -> recipeResult.canUseRecipe(world, getPlayer(), recipe));

            ItemStack craftingResult = craftingRecipe.map(recipe -> recipe.getCraftingResult(recipePattern)).orElse(ItemStack.EMPTY);

            setRecipe(!craftingResult.isEmpty() ? craftingRecipe.orElse(null) : null);
            setCraftingResult(craftingResult);
            this.pendingRecipe = null;
        }

        if (logic.updatePendingItems())
            return;

        boolean powered = world.isBlockPowered(pos);

        if (canCraft(powered))
            logic.tick();

        this.lastPowered = powered;
    }

    private boolean canCraft(boolean powered)
    {
        return getBlockState().get(FabricatorBlock.MODE).сanCraft(lastPowered, powered);
    }

    public int getColor(BlockState state)
    {
        return state.get(FabricatorBlock.MODE).rgb(world.isBlockPowered(pos));
    }

    public void dropItems()
    {
        InventoryUtils.dropInventoryItems(world, pos, inventory);
        logic.dropPendingItems();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);

        this.lastPowered = tag.getBoolean("LastPowered");

        if (tag.contains("Recipe", Constants.NBT.TAG_STRING))
            this.pendingRecipe = new ResourceLocation(tag.getString("Recipe"));

        InventoryUtils.read(recipePattern, tag.getCompound("RecipePattern"));
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        logic.load(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);

        IRecipe<?> recipe = recipeResult.getRecipeUsed();

        if (recipe != null)
            tag.putString("Recipe", recipe.getId().toString());

        tag.put("RecipePattern", InventoryUtils.write(recipePattern));
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
        if (side != Direction.UP)
            return capability.cast();
        return super.getCapability(type, side);
    }

    @Override
    protected void invalidateCaps()
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

    public CraftResultInventory recipeResult()
    {
        return recipeResult;
    }

    public ItemStackHandler inventory()
    {
        return inventory;
    }

    @Nullable
    public ICraftingRecipe recipe()
    {
        IRecipe<?> recipe = recipeResult.getRecipeUsed();
        return recipe instanceof ICraftingRecipe ? (ICraftingRecipe)recipe : null;
    }

    public void setRecipe(@Nullable ICraftingRecipe recipe)
    {
        recipeResult.setRecipeUsed(recipe);
        logic.recheckInventories();
    }

    public ItemStack craftingResult()
    {
        return recipeResult.getStackInSlot(0).copy();
    }

    public void setCraftingResult(ItemStack stack)
    {
        recipeResult.setInventorySlotContents(0, stack);
    }

    public FakePlayer getPlayer()
    {
        if (player == null)
            this.player = FakePlayerFactory.get((ServerWorld)world, PROFILE);
        return player;
    }

    public FabricatorMode mode()
    {
        return getBlockState().get(FabricatorBlock.MODE);
    }

    public void setMode(FabricatorMode mode)
    {
        world.setBlockState(pos, getBlockState().with(FabricatorBlock.MODE, mode), Constants.BlockFlags.BLOCK_UPDATE);
    }
}
