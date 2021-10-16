package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.init.ZYLang;

import java.util.List;

public class FluidSourceComponentProvider implements IComponentProvider
{
    public static final FluidSourceComponentProvider INSTANCE = new FluidSourceComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fluid_source");

    @Override
    public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY) && accessor.getBlock() instanceof IFluidSource)
        {
            FluidStack fluid = ((IFluidSource)accessor.getBlock()).getFluid(accessor.getBlockState(), accessor.getWorld(), accessor.getPosition(), accessor.getSide());

            if (!fluid.isEmpty())
                tooltip.add(ZYLang.SOURCE_FLUID_LABEL.plainCopy()
                        .append(ZYLang.copy(ZYLang.FLUID_INFO, fluid.getDisplayName(), fluid.getAmount())));
        }
    }
}
