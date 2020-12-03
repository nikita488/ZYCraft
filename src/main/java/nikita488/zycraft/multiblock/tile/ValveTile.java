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
import nikita488.zycraft.multiblock.io.IOType;
import nikita488.zycraft.util.BlockUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ValveTile extends MultiChildTile implements ITickableTileEntity
{
    private static final Direction[] VALUES = Direction.values();
    private IOType io = IOType.ALL_IN;
    private FluidStack storedFluid = FluidStack.EMPTY;

    public ValveTile(TileEntityType<ValveTile> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
        if (world.isRemote || parentMultiBlocks.isEmpty())
            return;

        Optional<IFluidHandler> multiCap = getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null, -1).resolve();

        if (!multiCap.isPresent())
            return;

        IFluidHandler multiHandler = multiCap.get();
        FluidStack containerFluid = multiHandler.getFluidInTank(0);

        if (io.isOutput())
        {
            if (containerFluid.getAmount() <= 0)
                return;
        }
        else
        {
            if (multiHandler.getTankCapacity(0) - containerFluid.getAmount() <= 0)
                return;
        }

        for (Direction side : VALUES)
        {
            BlockPos adjPos = pos.offset(side);
            BlockState adjState = world.getBlockState(adjPos);
            Block adjBlock = adjState.getBlock();

            if (io.isOutput())
            {
                if (adjBlock instanceof IMultiFluidVoid)
                {
                    int toDrain = ((IMultiFluidVoid)adjBlock).drain(world, pos, side);

                    if (toDrain > 0)
                        multiHandler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                }
                else if (adjState.hasTileEntity())
                {
                    TileEntity tile = world.getTileEntity(adjPos);

                    if (tile == null)
                        continue;

                    Optional<IFluidHandler> capability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).resolve();

                    if (!capability.isPresent())
                        continue;

                    IFluidHandler handler = capability.get();
                    FluidStack drained = multiHandler.drain(200, IFluidHandler.FluidAction.SIMULATE);
                    int filled = handler.fill(drained, IFluidHandler.FluidAction.SIMULATE);

                    if (filled <= 0)
                        continue;

                    drained = multiHandler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    handler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                }
            }
            else
            {
                if (adjBlock instanceof IMultiFluidSource)
                {
                    FluidStack toFill = ((IMultiFluidSource)adjBlock).fill(world, pos, side);

                    if (!toFill.isEmpty())
                        multiHandler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                }
                else
                {
                    FluidState fluidState = adjState.getFluidState();

                    if (!fluidState.isEmpty() && fluidState.isSource() && fluidState.isTagged(FluidTags.WATER))
                        multiHandler.fill(new FluidStack(Fluids.WATER, 50), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
        if (!removed && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return getMultiCapability(capability, side, -1);

        return super.getCapability(capability, side);
    }

    public IOType getIO()
    {
        return io;
    }
}
