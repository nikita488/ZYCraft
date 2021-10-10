package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.api.fluid.IFluidVoid;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;

@WailaPlugin
public class ZYHwylaPluign implements IWailaPlugin
{
    @Override
    public void register(IRegistrar registrar)
    {
        registrar.addConfig(FluidSourceComponentProvider.KEY, true);
        registrar.addConfig(FluidVoidComponentProvider.KEY, true);
        registrar.addConfig(FabricatorComponentProvider.KEY, true);
        registrar.addConfig(ValveComponentProvider.KEY, true);
        registrar.addConfig(ItemIOComponentProvider.KEY, true);
        registrar.addConfig(FluidVoidComponentProvider.KEY, true);

        registrar.registerComponentProvider(FluidSourceComponentProvider.INSTANCE, TooltipPosition.BODY, IFluidSource.class);
        registrar.registerComponentProvider(FluidVoidComponentProvider.INSTANCE, TooltipPosition.BODY, IFluidVoid.class);
        registrar.registerComponentProvider(FabricatorComponentProvider.INSTANCE, TooltipPosition.BODY, FabricatorBlock.class);
        registrar.registerComponentProvider(ValveComponentProvider.INSTANCE, TooltipPosition.BODY, ValveBlock.class);
        registrar.registerComponentProvider(ItemIOComponentProvider.INSTANCE, TooltipPosition.BODY, ItemIOBlock.class);

        registrar.registerBlockDataProvider(ValveComponentProvider.INSTANCE, ValveBlock.class);
    }
}
