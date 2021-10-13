package nikita488.zycraft.compat.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ValveTile;

import java.util.function.Function;

public class ZYProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void>
{
    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        return null;
    }

    @Override
    public String getID()
    {
        return ZYCraft.string("default");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, PlayerEntity player, World world, BlockState state, IProbeHitData data)
    {
        if (ZYBlocks.FABRICATOR.has(state))
            addFabricatorProbeInfo(info, state);
        else if (ZYBlocks.VALVE.has(state))
            addValveProbeInfo(info, world, state, data.getPos());
        else if (ZYBlocks.ITEM_IO.has(state))
            addItemIOProbeInfo(info, state);
        else if (state.getBlock() instanceof IFluidSource)
            addFluidSourceProbeInfo(info, (IFluidSource)state.getBlock(), world, state, data.getPos(), data.getSideHit());
        else if (state.getBlock() instanceof IFluidVoid)
            addFluidVoidProbeInfo(info, (IFluidVoid)state.getBlock(), world, state, data.getPos(), data.getSideHit());
    }

    private void addFabricatorProbeInfo(IProbeInfo info, BlockState state)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(FabricatorBlock.MODE).displayName()));
    }

    private void addValveProbeInfo(IProbeInfo info, World world, BlockState state, BlockPos pos)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(ValveBlock.IO_MODE).displayName()));

        TileEntity tile = world.getBlockEntity(pos);

        if (tile instanceof ValveTile)
        {
            FluidStack storedFluid = ((ValveTile)tile).getStoredFluid();

            if (!storedFluid.isEmpty())
                info.horizontal()
                        .text(CompoundText.create().label(ZYLang.STORED_FLUID_LABEL))
                        .tankSimple(storedFluid.getAmount(), storedFluid,
                                info.defaultProgressStyle()
                                        .prefix(storedFluid.getDisplayName().copy().append(" "))
                                        .suffix(new StringTextComponent(" mB")));
        }
    }

    private void addItemIOProbeInfo(IProbeInfo info, BlockState state)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(ItemIOBlock.IO_MODE).displayName()));
    }

    private void addFluidSourceProbeInfo(IProbeInfo info, IFluidSource source, World world, BlockState state, BlockPos pos, Direction side)
    {
        FluidStack fluid = source.getFluid(state, world, pos, side);

        if (!fluid.isEmpty())
            info.horizontal()
                    .text(CompoundText.create().label(ZYLang.SOURCE_FLUID_LABEL))
                    .tankSimple(fluid.getAmount(), fluid,
                            info.defaultProgressStyle()
                                    .prefix(fluid.getDisplayName().copy().append(" "))
                                    .suffix(new StringTextComponent(" mB")));
    }

    private void addFluidVoidProbeInfo(IProbeInfo info, IFluidVoid fluidVoid, World world, BlockState state, BlockPos pos, Direction side)
    {
        FluidStack fluidToDrain = fluidVoid.getFluidToDrain(state, world, pos, side);
        int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(state, world, pos, side) : 0;

        if (!fluidToDrain.isEmpty())
            info.horizontal()
                    .text(CompoundText.create().label(ZYLang.VOID_FLUID_LABEL))
                    .tankSimple(fluidToDrain.getAmount(), fluidToDrain,
                            info.defaultProgressStyle()
                                    .prefix(fluidToDrain.getDisplayName().copy().append(" "))
                                    .suffix(new StringTextComponent(" mB")));
        else if (drainAmount > 0)
            info.horizontal().text(CompoundText.create()
                    .label(ZYLang.VOID_FLUID_LABEL)
                    .info(ZYLang.copy(ZYLang.FLUID_INFO, "Any", drainAmount)));
    }
}
