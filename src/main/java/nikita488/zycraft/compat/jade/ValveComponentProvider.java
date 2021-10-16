package nikita488.zycraft.compat.jade;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ValveTile;

import java.util.List;

public class ValveComponentProvider implements IComponentProvider, IServerDataProvider<BlockEntity>
{
    public static final ValveComponentProvider INSTANCE = new ValveComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("valve");

    @Override
    public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (!config.get(KEY))
            return;

        tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(ValveBlock.IO_MODE).displayName()));

        CompoundTag data = accessor.getServerData();

        if (!data.contains("StoredFluid", Constants.NBT.TAG_COMPOUND))
            return;

        FluidStack storedFluid = FluidStack.loadFluidStackFromNBT(data.getCompound("StoredFluid"));

        if (!storedFluid.isEmpty())
            tooltip.add(ZYLang.STORED_FLUID_LABEL.plainCopy()
                    .append(ZYLang.copy(ZYLang.FLUID_INFO, storedFluid.getDisplayName(), storedFluid.getAmount())));
    }

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity)
    {
        if (blockEntity instanceof ValveTile)
        {
            FluidStack storedFluid = ((ValveTile)blockEntity).getStoredFluid();

            if (!storedFluid.isEmpty())
                data.put("StoredFluid", storedFluid.writeToNBT(new CompoundTag()));
        }
    }
}
