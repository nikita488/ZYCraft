package nikita488.zycraft.compat.jade;

import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;
import nikita488.zycraft.init.ZYLang;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class FabricatorComponentProvider implements IBlockComponentProvider
{
    public static final FabricatorComponentProvider INSTANCE = new FabricatorComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("fabricator");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(FabricatorBlock.MODE).displayName()));
    }

    @Override
    public ResourceLocation getUid()
    {
        return KEY;
    }
}
