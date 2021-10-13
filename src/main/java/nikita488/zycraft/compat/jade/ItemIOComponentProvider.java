package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;

import java.util.List;

public class ItemIOComponentProvider implements IComponentProvider
{
    public static final ItemIOComponentProvider INSTANCE = new ItemIOComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("item_io");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(ItemIOBlock.IO_MODE).displayName()));
    }
}
