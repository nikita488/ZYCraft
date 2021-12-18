package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.block.FluidSelectorBlock;
import nikita488.zycraft.block.FluidVoidBlock;
import nikita488.zycraft.block.PaintableViewerBlock;
import nikita488.zycraft.block.ZychoriumWaterBlock;
import nikita488.zycraft.block.entity.ColorableBlockEntity;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;

@WailaPlugin
public class ZYHwylaPluign implements IWailaPlugin
{
    @Override
    public void register(IRegistrar registrar)
    {
        registrar.addConfig(ColorableComponentProvider.KEY, true);
        registrar.addConfig(FluidSourceComponentProvider.KEY, true);
        registrar.addConfig(FluidVoidComponentProvider.KEY, true);
        registrar.addConfig(FabricatorComponentProvider.KEY, true);
        registrar.addConfig(ValveComponentProvider.KEY, true);
        registrar.addConfig(ItemIOComponentProvider.KEY, true);
        registrar.addConfig(FluidVoidComponentProvider.KEY, true);

        registrar.registerComponentProvider(ColorableComponentProvider.INSTANCE, TooltipPosition.BODY, ColorableBlock.class);
        registrar.registerComponentProvider(ColorableComponentProvider.INSTANCE, TooltipPosition.BODY, PaintableViewerBlock.class);
        registrar.registerComponentProvider(FluidSourceComponentProvider.INSTANCE, TooltipPosition.BODY, ZychoriumWaterBlock.class);
        registrar.registerComponentProvider(FluidSourceComponentProvider.INSTANCE, TooltipPosition.BODY, FluidSelectorBlock.class);
        registrar.registerComponentProvider(FluidVoidComponentProvider.INSTANCE, TooltipPosition.BODY, FluidVoidBlock.class);
        registrar.registerComponentProvider(FabricatorComponentProvider.INSTANCE, TooltipPosition.BODY, FabricatorBlock.class);
        registrar.registerComponentProvider(ValveComponentProvider.INSTANCE, TooltipPosition.BODY, ValveBlock.class);
        registrar.registerComponentProvider(ItemIOComponentProvider.INSTANCE, TooltipPosition.BODY, ItemIOBlock.class);

        registrar.registerBlockDataProvider(ColorableComponentProvider.INSTANCE, ColorableBlockEntity.class);
        registrar.registerBlockDataProvider(ValveComponentProvider.INSTANCE, ValveBlockEntity.class);
    }
}
