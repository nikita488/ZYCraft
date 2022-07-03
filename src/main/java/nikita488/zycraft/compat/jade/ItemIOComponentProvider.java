package nikita488.zycraft.compat.jade;

import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ItemIOComponentProvider implements IBlockComponentProvider
{
    public static final ItemIOComponentProvider INSTANCE = new ItemIOComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("item_io");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(ItemIOBlock.IO_MODE).displayName()));
    }

    @Override
    public ResourceLocation getUid()
    {
        return KEY;
    }
}
