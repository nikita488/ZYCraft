package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYLang;

import java.util.List;

public class FabricatorComponentProvider implements IComponentProvider
{
    public static final FabricatorComponentProvider INSTANCE = new FabricatorComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fabricator");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(ZYLang.MODE_LABEL.copyRaw().appendSibling(accessor.getBlockState().get(FabricatorBlock.MODE).displayName()));
    }
}
