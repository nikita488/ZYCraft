package nikita488.zycraft.compat.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import nikita488.zycraft.block.FabricatorBlock;

@WailaPlugin
public class ZYHwylaPluign implements IWailaPlugin
{
    @Override
    public void register(IRegistrar registrar)
    {
        registrar.addConfig(FabricatorModeProvider.ID, true);
        registrar.registerComponentProvider(FabricatorModeProvider.INSTANCE, TooltipPosition.BODY, FabricatorBlock.class);
    }
}
