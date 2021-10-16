package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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

public class ItemIOTile extends MultiInterfaceTile implements TickableBlockEntity, IItemHandlerModifiable
{
    private final Supplier<ItemIOMode> mode = () -> getBlockState().getValue(ItemIOBlock.IO_MODE);
    private LazyOptional<IItemHandler> capability = LazyOptional.empty();
    private IItemHandler inventory = EmptyHandler.INSTANCE;
    @Nullable
    private IMultiItemIOHandler ioHandler;

    public ItemIOTile(BlockEntityType<?> type)
    {
        super(type);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!player.isShiftKeyDown())
            return super.use(state, level, pos, player, hand, hitResult);

        nextMode(state);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private void nextMode(BlockState state)
    {
        BlockState lastState = state;

        state = state.cycle(ItemIOBlock.IO_MODE);

        while (!isSupported(state.getValue(ItemIOBlock.IO_MODE)))
            state = state.cycle(ItemIOBlock.IO_MODE);

        if (state == lastState)
            return;

        if (ioHandler != null)
        {
            ItemIOMode newMode = state.getValue(ItemIOBlock.IO_MODE);

            ioHandler.setInactive(mode.get());
            ioHandler.setActive(newMode);
            capability.invalidate();
            this.inventory = ioHandler.access(newMode);
            this.capability = LazyOptional.of(() -> this);
        }

        level.setBlockAndUpdate(worldPosition, state);
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
        if (!level.isClientSide() && capability.isPresent())
            update();
    }

    @Override
    protected void processRelativePos(BlockPos pos, Direction side)
    {
        if (mode.get().isOutput())
            eject(pos, side);
    }

    private void eject(BlockPos pos, Direction side)
    {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity == null)
            return;

        LazyOptional<IItemHandler> capability = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());

        if (!capability.isPresent())
            return;

        IItemHandler inventory = capability.orElse(EmptyHandler.INSTANCE);

        for (int slot = 0; slot < this.inventory.getSlots(); slot++)
        {
            ItemStack stack = this.inventory.extractItem(slot, Integer.MAX_VALUE, true);

            if (stack.isEmpty())
                continue;

            ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, stack, true);
            int amount = stack.getCount() - remainder.getCount();

            if (amount <= 0)
                continue;

            stack = this.inventory.extractItem(slot, amount, false);
            ItemHandlerHelper.insertItemStacked(inventory, stack, false);
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

        updateNeighbourForOutputSignal();
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
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
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
        return !remove && capability.isPresent() && type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? capability.cast() : super.getCapability(type, side);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        capability.invalidate();
    }
}
