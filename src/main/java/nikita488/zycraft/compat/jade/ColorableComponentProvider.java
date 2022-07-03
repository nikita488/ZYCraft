package nikita488.zycraft.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.init.ZYLang;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ColorableComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity>
{
    public static final ColorableComponentProvider INSTANCE = new ColorableComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("colorable");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (!config.get(KEY))
            return;

        CompoundTag data = accessor.getServerData();
        int color = data.getInt("Color");

        tooltip.add(ZYLang.CURRENT_COLOR_LABEL);
        tooltip.add(ZYLang.copy(ZYLang.RED_INFO, (color >> 16) & 255));
        tooltip.add(ZYLang.copy(ZYLang.GREEN_INFO, (color >> 8) & 255));
        tooltip.add(ZYLang.copy(ZYLang.BLUE_INFO, color & 255));
    }

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails)
    {
        if (blockEntity instanceof IColorable colorable)
            data.putInt("Color", colorable.getColor(blockEntity.getBlockState(), level, blockEntity.getBlockPos()));
    }

    @Override
    public ResourceLocation getUid()
    {
        return KEY;
    }
}
