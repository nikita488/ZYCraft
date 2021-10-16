package nikita488.zycraft.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.util.Color;

public class FluidSelectorTile extends ZYTile
{
    private float[] hsv;
    private FluidStack selectedFluid = FluidStack.EMPTY;

    public FluidSelectorTile(BlockEntityType<?> type)
    {
        super(type);
    }

    public int getColor()
    {
        float brightness = (15 - level.getBestNeighborSignal(worldPosition)) / 15F;
        return hsv != null ? Color.hsvToRGB(hsv[0], hsv[1], hsv[2] * brightness) : 0xFFFFFF;
    }

    @Override
    public void load(BlockState state, CompoundTag tag)
    {
        super.load(state, tag);

        if (tag.contains("SelectedFluid", Constants.NBT.TAG_COMPOUND))
            this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("SelectedFluid"));
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        super.save(tag);

        if (!selectedFluid.isEmpty())
            tag.put("SelectedFluid", selectedFluid.writeToNBT(new CompoundTag()));

        return tag;
    }

    @Override
    public void decode(CompoundTag tag)
    {
        this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag);

        if (selectedFluid.isEmpty())
            return;

        int color = selectedFluid.getFluid().getAttributes().getColor(selectedFluid);
        this.hsv = Color.rgbToHSV((color >> 16) & 255, (color >> 8) & 255, color & 255);
    }

    @Override
    public void decodeUpdate(CompoundTag tag)
    {
        super.decodeUpdate(tag);
        blockChanged();
    }

    @Override
    public void encode(CompoundTag tag)
    {
        if (!selectedFluid.isEmpty())
            selectedFluid.writeToNBT(tag);
    }

    public FluidStack getSelectedFluid()
    {
        return selectedFluid;
    }

    public void setSelectedFluid(FluidStack stack)
    {
        if (selectedFluid.isFluidEqual(stack))
            return;

        this.selectedFluid = stack;
        selectedFluid.setAmount(150);
        setChanged();
        sendUpdated();
    }
}
