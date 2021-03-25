package nikita488.zycraft.multiblock.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import nikita488.zycraft.multiblock.IMultiFluidSource;
import nikita488.zycraft.multiblock.IMultiFluidVoid;
import nikita488.zycraft.multiblock.IPressurizedFluidHandler;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.block.ValveBlock;
import nikita488.zycraft.multiblock.io.IOType;
import nikita488.zycraft.multiblock.tank.MultiFluidTank;
import nikita488.zycraft.util.BlockUtils;
import nikita488.zycraft.util.FluidUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ValveTile extends MultiChildTile implements ITickableTileEntity
{
    private IOType io = IOType.ALL_IN;
    private FluidStack storedFluid = FluidStack.EMPTY;

    public ValveTile(TileEntityType<ValveTile> type)
    {
        super(type);
    }

    @Override
    public void onMultiValidation(MultiBlock multiBlock)
    {
        super.onMultiValidation(multiBlock);

        if (storedFluid.isEmpty())
            return;

        Optional<IFluidHandler> capability = multiBlock.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this)
                .filter(handler -> handler.isFluidValid(0, storedFluid));

        if (!capability.isPresent())
            return;

        capability.get().fill(storedFluid, IFluidHandler.FluidAction.EXECUTE);
        this.storedFluid = FluidStack.EMPTY;
        markDirty();
    }

    @Override
    public void tick()
    {
        if (world.isRemote || parentMultiBlocks.isEmpty())
            return;

        balanceTanks();

        Optional<IFluidHandler> capability = getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, -1).resolve();

        if (!capability.isPresent())
            return;

        IFluidHandler handler = capability.get();
        FluidStack containedFluid = handler.getFluidInTank(0);

        if (io.isOutput())
        {
            if (containedFluid.isEmpty())
                return;
        }
        else
        {
            if (handler.getTankCapacity(0) - containedFluid.getAmount() <= 0)
                return;
        }

        BlockState valveState = getBlockState();

        ValveBlock.SIDES.forEach((side, property) ->
        {
            if (!valveState.get(property))
                return;

            BlockPos adjPos = pos.offset(side);

            if (!world.isBlockPresent(adjPos))
                return;

            BlockState adjState = world.getBlockState(adjPos);
            Block adjBlock = adjState.getBlock();

            if (io.isOutput())
            {
                if (adjBlock instanceof IMultiFluidVoid)
                {
                    int toDrain = ((IMultiFluidVoid)adjBlock).drain(world, pos, side);

                    if (toDrain > 0)
                        handler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                }
                else if (adjState.hasTileEntity())
                {
                    TileEntity tile = world.getTileEntity(adjPos);

                    if (tile != null)
                        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())
                                .ifPresent(adjHandler -> FluidUtils.transferFromTo(handler, adjHandler, 200));
                }
            }
            else
            {
                FluidStack toFill = FluidStack.EMPTY;
                FluidState fluidState = adjState.getFluidState();

                if (adjBlock instanceof IMultiFluidSource)
                    toFill = ((IMultiFluidSource)adjBlock).fill(world, pos, side);
                else if (!fluidState.isEmpty() && fluidState.isSource() && fluidState.isTagged(FluidTags.WATER))
                    toFill = new FluidStack(Fluids.WATER, 50);

                if (!toFill.isEmpty())
                    handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    private void balanceTanks()
    {
        Optional<IFluidHandler> fromCap = getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, 0)
                .filter(handler -> handler instanceof IPressurizedFluidHandler);
        Optional<IFluidHandler> toCap = getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, 1)
                .filter(handler -> handler instanceof IPressurizedFluidHandler);

        if (!fromCap.isPresent() || !toCap.isPresent())
            return;

        IPressurizedFluidHandler fromHandler = (IPressurizedFluidHandler)fromCap.get();
        IPressurizedFluidHandler toHandler = (IPressurizedFluidHandler)toCap.get();

        fromHandler.applyPressure(toHandler);
    }

    public void toggleIO()
    {
        this.io = io == IOType.ALL_IN ? IOType.ALL_OUT : IOType.ALL_IN;
        BlockUtils.scheduleTileUpdate(world, pos, getBlockState());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        this.io = IOType.load(tag);
        this.storedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("StoredFluid"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        io.save(tag);
        if (!storedFluid.isEmpty())
            tag.put("StoredFluid", storedFluid.writeToNBT(new CompoundNBT()));
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.io = IOType.decode(tag);
        BlockUtils.scheduleRenderUpdate(world, pos);
    }

    @Override
    public void encode(CompoundNBT tag)
    {
        io.encode(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nullable Direction side)
    {
        if (!removed && type == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return getMultiCapability(type, -1);

        return super.getCapability(type, side);
    }

    public IOType getIO()
    {
        return io;
    }

    public FluidStack getStoredFluid()
    {
        return storedFluid;
    }

    public void setStoredFluid(FluidStack fluid)
    {
        this.storedFluid = fluid;
    }
}
