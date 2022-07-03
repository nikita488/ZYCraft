package nikita488.zycraft.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.block.entity.ValveBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class ValveComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity>
{
    public static final ValveComponentProvider INSTANCE = new ValveComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("valve");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config)
    {
        if (!config.get(KEY))
            return;

        tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(ValveBlock.IO_MODE).displayName()));

        CompoundTag data = accessor.getServerData();

        if (!data.contains("StoredFluid", Tag.TAG_COMPOUND))
            return;

        FluidStack storedFluid = FluidStack.loadFluidStackFromNBT(data.getCompound("StoredFluid"));

        if (!storedFluid.isEmpty())
            tooltip.add(ZYLang.STORED_FLUID_LABEL.plainCopy()
                    .append(ZYLang.copy(ZYLang.FLUID_INFO, storedFluid.getDisplayName(), storedFluid.getAmount())));
    }

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, BlockEntity blockEntity, boolean showDetails)
    {
        if (blockEntity instanceof ValveBlockEntity valve)
        {
            FluidStack storedFluid = valve.getStoredFluid();

            if (!storedFluid.isEmpty())
                data.put("StoredFluid", storedFluid.writeToNBT(new CompoundTag()));
        }
    }

    @Override
    public ResourceLocation getUid()
    {
        return KEY;
    }
}
