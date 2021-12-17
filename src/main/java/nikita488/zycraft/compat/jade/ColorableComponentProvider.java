package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.init.ZYLang;

import java.util.List;

public class ColorableComponentProvider implements IComponentProvider, IServerDataProvider<TileEntity>
{
    public static final ColorableComponentProvider INSTANCE = new ColorableComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("colorable");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (!config.get(KEY))
            return;

        CompoundNBT data = accessor.getServerData();
        int color = data.getInt("Color");

        tooltip.add(ZYLang.CURRENT_COLOR_LABEL);
        tooltip.add(ZYLang.copy(ZYLang.RED_INFO, (color >> 16) & 255));
        tooltip.add(ZYLang.copy(ZYLang.GREEN_INFO, (color >> 8) & 255));
        tooltip.add(ZYLang.copy(ZYLang.BLUE_INFO, color & 255));
    }

    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World level, TileEntity blockEntity)
    {
        if (blockEntity instanceof IColorable)
            data.putInt("Color", ((IColorable)blockEntity).getColor(blockEntity.getBlockState(), level, blockEntity.getBlockPos()));
    }
}
