package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYLang;

public class FabricatorComponentProvider implements IComponentProvider
{
    public static final FabricatorComponentProvider INSTANCE = new FabricatorComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fabricator");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(FabricatorBlock.MODE).displayName()));
    }
}
