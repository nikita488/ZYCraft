package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.init.ZYLang;

import java.util.List;

public class FluidVoidComponentProvider implements IComponentProvider
{
    public static final FluidVoidComponentProvider INSTANCE = new FluidVoidComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fluid_void");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY) && accessor.getBlock() instanceof IFluidVoid)
        {
            IFluidVoid fluidVoid = (IFluidVoid)accessor.getBlock();
            FluidStack fluidToDrain = fluidVoid.getFluidToDrain(accessor.getBlockState(), accessor.getWorld(), accessor.getPosition(), accessor.getSide());
            int drainAmount = fluidToDrain.isEmpty() ? fluidVoid.getDrainAmount(accessor.getBlockState(), accessor.getWorld(), accessor.getPosition(), accessor.getSide()) : 0;

            if (!fluidToDrain.isEmpty())
                tooltip.add(ZYLang.VOID_FLUID_LABEL.copyRaw()
                        .appendSibling(ZYLang.copy(ZYLang.FLUID_INFO, fluidToDrain.getDisplayName(), fluidToDrain.getAmount())));
            else if (drainAmount > 0)
                tooltip.add(ZYLang.VOID_FLUID_LABEL.copyRaw()
                        .appendSibling(ZYLang.copy(ZYLang.FLUID_INFO, "Any", drainAmount)));
        }
    }
}
