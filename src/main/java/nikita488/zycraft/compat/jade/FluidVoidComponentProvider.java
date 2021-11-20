package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.init.ZYLang;

public class FluidVoidComponentProvider implements IComponentProvider
{
    public static final FluidVoidComponentProvider INSTANCE = new FluidVoidComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fluid_void");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY) && accessor.getBlock() instanceof IFluidVoid fluidVoid)
        {
            FluidStack fluidToDrain = fluidVoid.getFluidToDrain(accessor.getBlockState(), accessor.getLevel(), accessor.getPosition(), accessor.getSide());
            int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(accessor.getBlockState(), accessor.getLevel(), accessor.getPosition(), accessor.getSide()) : 0;

            if (!fluidToDrain.isEmpty())
                tooltip.add(ZYLang.VOID_FLUID_LABEL.plainCopy()
                        .append(ZYLang.copy(ZYLang.FLUID_INFO, fluidToDrain.getDisplayName(), fluidToDrain.getAmount())));
            else if (drainAmount > 0)
                tooltip.add(ZYLang.VOID_FLUID_LABEL.plainCopy()
                        .append(ZYLang.copy(ZYLang.FLUID_INFO, "Any", drainAmount)));
        }
    }
}
