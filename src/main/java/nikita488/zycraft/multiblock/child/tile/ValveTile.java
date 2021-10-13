package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
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

public class ValveTile extends MultiInterfaceTile implements ITickableTileEntity, IFluidHandler
{
    private final Supplier<ValveIOMode> mode = () -> getBlockState().getValue(ValveBlock.IO_MODE);
    private LazyOptional<IFluidHandler> capability = LazyOptional.empty();
    private IFluidHandler mainTank = EmptyFluidHandler.INSTANCE;
    @Nullable
    private IMultiFluidHandler.Level fromTank, toTank;
    private FluidStack storedFluid = FluidStack.EMPTY;

    public ValveTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
        if (!level.isClientSide())
            if (capability.isPresent())
                update();
            else if (parentCount() > 1)
                balanceTanks();
    }

    @Override
    protected void processAdjacentPos(BlockPos pos, Direction side)
    {
        BlockState state = level.getBlockState(pos);

        if (mode.get().isOutput())
            eject(state, pos, side);
        else
            absorb(state, pos, side);
    }

    private void eject(BlockState adjState, BlockPos adjPos, Direction side)
    {
        if (adjState.getBlock() instanceof IFluidVoid)
        {
            IFluidVoid fluidVoid = (IFluidVoid)adjState.getBlock();
            FluidStack fluidToDrain = fluidVoid.getFluidToDrain(adjState, level, adjPos, side.getOpposite());
            int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(adjState, level, adjPos, side.getOpposite()) : 0;

            if (!fluidToDrain.isEmpty())
                mainTank.drain(fluidToDrain, IFluidHandler.FluidAction.EXECUTE);
            else if (drainAmount > 0)
                mainTank.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
        }
        else if (adjState.hasTileEntity())
        {
            TileEntity tile = level.getBlockEntity(adjPos);

            if (tile != null)
                tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())
                        .ifPresent(adjTank -> FluidUtils.transferFromTo(mainTank, adjTank, 200));
        }
    }

    private void absorb(BlockState adjState, BlockPos adjPos, Direction side)
    {
        FluidStack fluid = FluidStack.EMPTY;
        FluidState adjFluidState = adjState.getFluidState();

        if (adjState.getBlock() instanceof IFluidSource)
            fluid = ((IFluidSource)adjState.getBlock()).getFluid(adjState, level, adjPos, side.getOpposite());
        else if (adjFluidState.getType() == Fluids.WATER.delegate.get())
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
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        if (!mode.get().isOutput())
            return 0;

        int capacity = mainTank.getTankCapacity(0);
        float proportion = capacity > 0 ? (float)mainTank.getFluidInTank(0).getAmount() / capacity : 0;

        return MathHelper.floor(proportion * 14) + (proportion > 0 ? 1 : 0);
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
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);

        if (tag.contains("StoredFluid", Constants.NBT.TAG_COMPOUND))
            this.storedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("StoredFluid"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);

        if (!storedFluid.isEmpty())
            tag.put("StoredFluid", storedFluid.writeToNBT(new CompoundNBT()));

        return tag;
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
