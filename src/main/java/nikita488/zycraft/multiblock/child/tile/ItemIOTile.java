package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import nikita488.zycraft.block.state.properties.ItemIOMode;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.inventory.IMultiItemIOHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ItemIOTile extends MultiInterfaceTile implements ITickableTileEntity, IItemHandlerModifiable
{
    private final Supplier<ItemIOMode> mode = () -> getBlockState().get(ItemIOBlock.IO_MODE);
    private LazyOptional<IItemHandler> capability = LazyOptional.empty();
    private IItemHandler inventory = EmptyHandler.INSTANCE;
    @Nullable
    private IMultiItemIOHandler ioHandler;

    public ItemIOTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!player.isSneaking())
            return super.onBlockActivated(state, world, pos, player, hand, hit);

        nextMode(state);
        return ActionResultType.func_233537_a_(world.isRemote());
    }

    private void nextMode(BlockState state)
    {
        BlockState lastState = state;

        state = state.cycleValue(ItemIOBlock.IO_MODE);

        while (!isSupported(state.get(ItemIOBlock.IO_MODE)))
            state = state.cycleValue(ItemIOBlock.IO_MODE);

        if (state == lastState)
            return;

        if (ioHandler != null)
        {
            ItemIOMode newMode = state.get(ItemIOBlock.IO_MODE);

            ioHandler.setInactive(mode.get());
            ioHandler.setActive(newMode);
            capability.invalidate();
            this.inventory = ioHandler.access(newMode);
            this.capability = LazyOptional.of(() -> this);
        }

        world.setBlockState(pos, state);
    }

    private boolean isSupported(ItemIOMode mode)
    {
        if (!capability.isPresent())
            return true;
        else if (ioHandler != null)
            return ioHandler.isSupported(mode);
        else
            return mode == ItemIOMode.ANY;
    }

    @Override
    public void tick()
    {
        if (!world.isRemote() && capability.isPresent())
            update();
    }

    @Override
    protected void processAdjacentPos(BlockPos pos, Direction side)
    {
        if (mode.get().isOutput())
            eject(pos, side);
    }

    private void eject(BlockPos adjPos, Direction side)
    {
        TileEntity adjTile = world.getTileEntity(adjPos);

        if (adjTile == null)
            return;

        LazyOptional<IItemHandler> adjCapability = adjTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());

        if (!adjCapability.isPresent())
            return;

        IItemHandler adjInventory = adjCapability.orElse(EmptyHandler.INSTANCE);

        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = inventory.extractItem(slot, Integer.MAX_VALUE, true);

            if (stack.isEmpty())
                continue;

            ItemStack remainder = ItemHandlerHelper.insertItemStacked(adjInventory, stack, true);
            int amount = stack.getCount() - remainder.getCount();

            if (amount <= 0)
                continue;

            stack = inventory.extractItem(slot, amount, false);
            ItemHandlerHelper.insertItemStacked(adjInventory, stack, false);
        }
    }

    private void cacheItemHandler()
    {
        if (!capability.isPresent())
        {
            LazyOptional<IItemHandler> capability = getMultiCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

            if (!capability.isPresent())
                return;

            this.capability = LazyOptional.of(() -> this);
            this.inventory = capability.orElse(EmptyHandler.INSTANCE);

            if (inventory instanceof IMultiItemIOHandler)
                this.ioHandler = (IMultiItemIOHandler)inventory;

            ItemIOMode mode = this.mode.get();

            if (!isSupported(mode))
            {
                nextMode(getBlockState());
            }
            else if (ioHandler != null)
            {
                ioHandler.setActive(mode);
                this.inventory = ioHandler.access(mode);
            }
        }
        else
        {
            if (ioHandler != null)
                ioHandler.setInactive(mode.get());

            capability.invalidate();
            this.capability = LazyOptional.empty();
            this.inventory = EmptyHandler.INSTANCE;
            this.ioHandler = null;
        }

        updateComparatorOutputLevel();
    }

    @Override
    public void onMultiValidation(MultiBlock multiBlock)
    {
        super.onMultiValidation(multiBlock);
        cacheItemHandler();
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);
        cacheItemHandler();
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return inventory.getSlots() > 0 ? ItemHandlerHelper.calcRedstoneFromInventory(inventory) : 0;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        if (inventory instanceof IItemHandlerModifiable)
            ((IItemHandlerModifiable)inventory).setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots()
    {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        return mode.get().isOutput() ? stack : inventory.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        ItemIOMode mode = this.mode.get();
        return mode == ItemIOMode.ANY || mode.isOutput() ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return inventory.isItemValid(slot, stack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nullable Direction side)
    {
        return !removed && capability.isPresent() && type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? capability.cast() : super.getCapability(type, side);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        capability.invalidate();
    }
}
