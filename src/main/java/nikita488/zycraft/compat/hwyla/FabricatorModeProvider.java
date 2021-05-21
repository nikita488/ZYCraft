package nikita488.zycraft.compat.hwyla;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.block.FabricatorBlock;

import java.util.List;

public class FabricatorModeProvider implements IComponentProvider
{
    public static final FabricatorModeProvider INSTANCE = new FabricatorModeProvider();
    public static final ResourceLocation KEY = ZYCraft.id("mode");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (config.get(KEY))
            tooltip.add(new StringTextComponent("Mode: " + accessor.getBlockState().get(FabricatorBlock.MODE).displayName()));
    }
}
