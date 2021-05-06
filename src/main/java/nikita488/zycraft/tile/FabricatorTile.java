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
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;
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
            return recipe.getType() == IRecipeType.CRAFTING && !recipe.isDynamic() && !recipe.getIngredients().isEmpty() && !recipe.getRecipeOutput().isEmpty() && recipe.canFit(3, 3);
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
    private FakePlayer player;
    private boolean lastPowered;
    private int reloadCount = DataPackReloadCounter.INSTANCE.count();

    public FabricatorTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
        if (world.isRemote())
            return;

        if (reloadCount != DataPackReloadCounter.INSTANCE.count())
        {
            if (recipeResult.getRecipeUsed() != null)
            {
                System.out.println(recipeResult.getRecipeUsed());
                System.out.println(world.getRecipeManager().getRecipe(recipeResult.getRecipeUsed().getId()).orElse(null));
            }
            ZYCraft.LOGGER.info("Reloaded recipe for {}", pos);
            this.reloadCount = DataPackReloadCounter.INSTANCE.count();
        }

        if (!logic.updatePendingItems())
            return;

        boolean powered = world.isBlockPowered(pos);

        if (powered && !lastPowered)
            logic.tick();

        this.lastPowered = powered;
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

        ZYCraft.LOGGER.info("World {} Pos {}", world, pos);
        InventoryUtils.read(recipePattern, tag.getCompound("RecipePattern"));

        //TODO: World is null when loading
        if (false && tag.contains("Recipe", Constants.NBT.TAG_STRING))
        {
            Optional<ICraftingRecipe> craftingRecipe = world.getRecipeManager().getRecipe(new ResourceLocation(tag.getString("Recipe")))
                    .filter(recipe -> recipeResult.canUseRecipe(world, getPlayer(), recipe))
                    .map(recipe -> (ICraftingRecipe)recipe)
                    .filter(recipe -> recipe.matches(recipePattern, world));

            if (!craftingRecipe.isPresent())
                craftingRecipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, recipePattern, world);

            ItemStack recipeResult = craftingRecipe
                    .map(recipe -> recipe.getCraftingResult(recipePattern))
                    .orElse(ItemStack.EMPTY);
            //TODO: If recipe was not found, find first that matches recipe pattern

            setRecipe(craftingRecipe.orElse(null));
            setCraftingResult(recipeResult);
        }

        inventory.deserializeNBT(tag.getCompound("Inventory"));
        logic.load(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);

        IRecipe<?> recipe = recipeResult.getRecipeUsed();

        tag.put("RecipePattern", InventoryUtils.write(recipePattern));

        if (recipe != null)
            tag.putString("Recipe", recipe.getId().toString());

        tag.put("Inventory", inventory.serializeNBT());
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
}
