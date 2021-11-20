package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.init.ZYLang;

public class FluidSourceComponentProvider implements IComponentProvider
{
    public static final FluidSourceComponentProvider INSTANCE = new FluidSourceComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fluid_source");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY) && accessor.getBlock() instanceof IFluidSource source)
        {
            FluidStack fluid = source.getFluid(accessor.getBlockState(), accessor.getLevel(), accessor.getPosition(), accessor.getSide());

            if (!fluid.isEmpty())
                tooltip.add(ZYLang.SOURCE_FLUID_LABEL.plainCopy()
                        .append(ZYLang.copy(ZYLang.FLUID_INFO, fluid.getDisplayName(), fluid.getAmount())));
        }
    }
}
