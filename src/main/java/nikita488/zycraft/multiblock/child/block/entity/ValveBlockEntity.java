package nikita488.zycraft.multiblock.child.block.entity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.block.state.properties.ValveIOMode;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.fluid.IMultiFluidHandler;
import nikita488.zycraft.util.FluidUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class ValveBlockEntity extends MultiInterfaceBlockEntity implements IFluidHandler
{
    private final Supplier<ValveIOMode> mode = () -> getBlockState().getValue(ValveBlock.IO_MODE);
    private LazyOptional<IFluidHandler> capability = LazyOptional.empty();
    private IFluidHandler mainTank = EmptyFluidHandler.INSTANCE;
    @Nullable
    private IMultiFluidHandler.Level fromTank, toTank;
    private FluidStack storedFluid = FluidStack.EMPTY;

    public ValveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ValveBlockEntity valve)
    {
        if (valve.capability.isPresent())
            valve.update();
        else if (valve.parentCount() > 1)
            valve.balanceTanks();
    }

    @Override
    protected void processRelativePos(BlockPos pos, Direction side)
    {
        BlockState state = level.getBlockState(pos);

        if (mode.get().isOutput())
            eject(state, pos, side);
        else
            absorb(state, pos, side);
    }

    private void eject(BlockState state, BlockPos pos, Direction side)
    {
        if (state.getBlock() instanceof IFluidVoid fluidVoid)
        {
            FluidStack fluidToDrain = fluidVoid.getFluidToDrain(state, level, pos, side.getOpposite());
            int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(state, level, pos, side.getOpposite()) : 0;

            if (!fluidToDrain.isEmpty())
                mainTank.drain(fluidToDrain, IFluidHandler.FluidAction.EXECUTE);
            else if (drainAmount > 0)
                mainTank.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
        }
        else if (state.hasBlockEntity())
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity != null)
                blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())
                        .ifPresent(tank -> FluidUtils.transferFromTo(mainTank, tank, 200));
        }
    }

    private void absorb(BlockState state, BlockPos pos, Direction side)
    {
        FluidStack fluid = FluidStack.EMPTY;
        FluidState fluidState = state.getFluidState();

        if (state.getBlock() instanceof IFluidSource source)
            fluid = source.getFluid(state, level, pos, side.getOpposite());
        else if (fluidState.getType() == Fluids.WATER.delegate.get())
            fluid = new FluidStack(Fluids.WATER, 50);

        if (!fluid.isEmpty())
            mainTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
    }

    private void balanceTanks()
    {
        if (fromTank == null || toTank == null)
            return;

        FluidStack fluid = fromTank.getFluidInTank(0);
        FluidStack targetFluid = toTank.getFluidInTank(0);

        if (fluid.isEmpty() && targetFluid.isEmpty())
            return;

        if (fluid.isEmpty() || targetFluid.isEmpty() || fluid.isFluidEqual(targetFluid))
            fromTank.applyPressure(toTank, worldPosition.getY());
    }

    private Optional<IMultiFluidHandler.Level> accessTankLevel()
    {
        return accessTankLevel(FIRST_AND_ONLY);
    }

    private Optional<IMultiFluidHandler.Level> accessTankLevel(int tankIndex)
    {
        return getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, tankIndex)
                .filter(handler -> handler instanceof IMultiFluidHandler)
                .map(handler -> ((IMultiFluidHandler)handler).level(worldPosition.getY()));
    }

    private void cacheFluidHandlers()
    {
        if (!capability.isPresent())
        {
            Optional<IMultiFluidHandler.Level> capability = accessTankLevel();

            if (!capability.isPresent())
                return;

            this.capability = LazyOptional.of(() -> this);
            this.mainTank = capability.get();
            this.fromTank = this.toTank = null;
        }
        else
        {
            capability.invalidate();
            this.capability = LazyOptional.empty();
            this.mainTank = EmptyFluidHandler.INSTANCE;

            if (parentCount() > 1)
            {
                this.fromTank = accessTankLevel(0).orElse(null);
                this.toTank = accessTankLevel(1).orElse(null);
            }
        }

        updateNeighbourForOutputSignal();
    }

    @Override
    public void onMultiValidation(MultiBlock multiBlock)
    {
        super.onMultiValidation(multiBlock);
        cacheFluidHandlers();

        LazyOptional<IFluidHandler> capability = multiBlock.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, worldPosition);
        IFluidHandler handler = capability.orElse(EmptyFluidHandler.INSTANCE);

        if (capability.isPresent() && !storedFluid.isEmpty() && handler.fill(storedFluid, FluidAction.SIMULATE) > 0)
        {
            int remainder = storedFluid.getAmount() - handler.fill(storedFluid, FluidAction.EXECUTE);

            setStoredFluid(remainder > 0 ? Util.make(storedFluid.copy(), stack -> stack.setAmount(remainder)) : FluidStack.EMPTY);
        }
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);
        cacheFluidHandlers();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        if (!mode.get().isOutput())
            return 0;

        int capacity = mainTank.getTankCapacity(0);
        float proportion = capacity > 0 ? (float)mainTank.getFluidInTank(0).getAmount() / capacity : 0;

        return Mth.floor(proportion * 14) + (proportion > 0 ? 1 : 0);
    }

    @Override
    public int getTanks()
    {
        return mainTank.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return mainTank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return mainTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return mainTank.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        return mode.get().isOutput() ? 0 : mainTank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        return mode.get().isOutput() ? mainTank.drain(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        return mode.get().isOutput() ? mainTank.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        if (tag.contains("StoredFluid", Tag.TAG_COMPOUND))
            this.storedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("StoredFluid"));
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        if (!storedFluid.isEmpty())
            tag.put("StoredFluid", storedFluid.writeToNBT(new CompoundTag()));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nullable Direction side)
    {
        return !remove && capability.isPresent() && type == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? capability.cast() : super.getCapability(type, side);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        capability.invalidate();
    }

    public FluidStack getStoredFluid()
    {
        return storedFluid;
    }

    public void setStoredFluid(FluidStack stack)
    {
        this.storedFluid = stack;
        setChanged();
    }

    public boolean hasStoredFluid()
    {
        return !storedFluid.isEmpty();
    }
}
