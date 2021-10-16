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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.tile.ValveTile;

import java.util.List;

public class ValveComponentProvider implements IComponentProvider, IServerDataProvider<TileEntity>
{
    public static final ValveComponentProvider INSTANCE = new ValveComponentProvider();
    public static final ResourceLocation KEY = ZYCraft.id("valve");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config)
    {
        if (!config.get(KEY))
            return;

        tooltip.add(ZYLang.MODE_LABEL.plainCopy().append(accessor.getBlockState().getValue(ValveBlock.IO_MODE).displayName()));

        CompoundNBT data = accessor.getServerData();

        if (!data.contains("StoredFluid", Constants.NBT.TAG_COMPOUND))
            return;

        FluidStack storedFluid = FluidStack.loadFluidStackFromNBT(data.getCompound("StoredFluid"));

        if (!storedFluid.isEmpty())
            tooltip.add(ZYLang.STORED_FLUID_LABEL.plainCopy()
                    .append(ZYLang.copy(ZYLang.FLUID_INFO, storedFluid.getDisplayName(), storedFluid.getAmount())));
    }

    @Override
    public void appendServerData(CompoundNBT data, ServerPlayerEntity player, World level, TileEntity blockEntity)
    {
        if (blockEntity instanceof ValveTile)
        {
            FluidStack storedFluid = ((ValveTile)blockEntity).getStoredFluid();

            if (!storedFluid.isEmpty())
                data.put("StoredFluid", storedFluid.writeToNBT(new CompoundNBT()));
        }
    }
}
