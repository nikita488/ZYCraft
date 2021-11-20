package nikita488.zycraft.compat.theoneprobe;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;

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
    public ResourceLocation getID()
    {
        return ZYCraft.id("default");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data)
    {
        if (ZYBlocks.FABRICATOR.has(state))
            addFabricatorProbeInfo(info, state);
        else if (ZYBlocks.VALVE.has(state))
            addValveProbeInfo(info, level, state, data.getPos());
        else if (ZYBlocks.ITEM_IO.has(state))
            addItemIOProbeInfo(info, state);
        else if (state.getBlock() instanceof IFluidSource source)
            addFluidSourceProbeInfo(info, source, level, state, data.getPos(), data.getSideHit());
        else if (state.getBlock() instanceof IFluidVoid fluidVoid)
            addFluidVoidProbeInfo(info, fluidVoid, level, state, data.getPos(), data.getSideHit());
    }

    private void addFabricatorProbeInfo(IProbeInfo info, BlockState state)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(FabricatorBlock.MODE).displayName()));
    }

    private void addValveProbeInfo(IProbeInfo info, Level level, BlockState state, BlockPos pos)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(ValveBlock.IO_MODE).displayName()));

        if (level.getBlockEntity(pos) instanceof ValveBlockEntity valve)
        {
            FluidStack storedFluid = valve.getStoredFluid();

            if (!storedFluid.isEmpty())
                info.horizontal()
                        .text(CompoundText.create().label(ZYLang.STORED_FLUID_LABEL))
                        .tankSimple(storedFluid.getAmount(), storedFluid,
                                info.defaultProgressStyle()
                                        .prefix(storedFluid.getDisplayName().copy().append(" "))
                                        .suffix(new TextComponent(" mB")));
        }
    }

    private void addItemIOProbeInfo(IProbeInfo info, BlockState state)
    {
        info.horizontal().text(CompoundText.create().label(ZYLang.MODE_LABEL).info(state.getValue(ItemIOBlock.IO_MODE).displayName()));
    }

    private void addFluidSourceProbeInfo(IProbeInfo info, IFluidSource source, Level level, BlockState state, BlockPos pos, Direction side)
    {
        FluidStack fluid = source.getFluid(state, level, pos, side);

        if (!fluid.isEmpty())
            info.horizontal()
                    .text(CompoundText.create().label(ZYLang.SOURCE_FLUID_LABEL))
                    .tankSimple(fluid.getAmount(), fluid,
                            info.defaultProgressStyle()
                                    .prefix(fluid.getDisplayName().copy().append(" "))
                                    .suffix(new TextComponent(" mB")));
    }

    private void addFluidVoidProbeInfo(IProbeInfo info, IFluidVoid fluidVoid, Level level, BlockState state, BlockPos pos, Direction side)
    {
        FluidStack fluidToDrain = fluidVoid.getFluidToDrain(state, level, pos, side);
        int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(state, level, pos, side) : 0;

        if (!fluidToDrain.isEmpty())
            info.horizontal()
                    .text(CompoundText.create().label(ZYLang.VOID_FLUID_LABEL))
                    .tankSimple(fluidToDrain.getAmount(), fluidToDrain,
                            info.defaultProgressStyle()
                                    .prefix(fluidToDrain.getDisplayName().copy().append(" "))
                                    .suffix(new TextComponent(" mB")));
        else if (drainAmount > 0)
            info.horizontal().text(CompoundText.create()
                    .label(ZYLang.VOID_FLUID_LABEL)
                    .info(ZYLang.copy(ZYLang.FLUID_INFO, "Any", drainAmount)));
    }
}
