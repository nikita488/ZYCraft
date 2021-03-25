package nikita488.zycraft.multiblock.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import nikita488.zycraft.multiblock.io.IOType;
import nikita488.zycraft.util.BlockUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemIOTile extends MultiChildTile
{
    private IOType io = IOType.ALL;

    public ItemIOTile(TileEntityType<ItemIOTile> type)
    {
        super(type);
    }

    public void toggleIO()
    {
        this.io = io.next();
        BlockUtils.scheduleTileUpdate(world, pos, getBlockState());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        this.io = IOType.load(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        io.save(tag);
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.io = IOType.decode(tag);
        BlockUtils.scheduleRenderUpdate(world, pos);
    }

    @Override
    public void encode(CompoundNBT tag)
    {
        io.encode(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> type, @Nullable Direction side)
    {
        if (!removed && type == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return getMultiCapability(type, -1);

        return super.getCapability(type, side);
    }

    public IOType getIO()
    {
        return io;
    }
}
