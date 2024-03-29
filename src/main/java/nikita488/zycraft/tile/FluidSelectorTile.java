package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.util.Color;

import javax.annotation.Nullable;

public class FluidSelectorTile extends ZYTile
{
    @Nullable
    private float[] hsv;
    private FluidStack selectedFluid = FluidStack.EMPTY;

    public FluidSelectorTile(TileEntityType<?> type)
    {
        super(type);
    }

    public int getColor()
    {
        float brightness = (15 - level.getBestNeighborSignal(worldPosition)) / 15F;
        return hsv != null ? Color.hsvToRGB(hsv[0], hsv[1], hsv[2] * brightness) : 0xFFFFFF;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);

        if (tag.contains("SelectedFluid", Constants.NBT.TAG_COMPOUND))
            this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("SelectedFluid"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);

        if (!selectedFluid.isEmpty())
            tag.put("SelectedFluid", selectedFluid.writeToNBT(new CompoundNBT()));

        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag);

        if (selectedFluid.isEmpty())
            return;

        int color = selectedFluid.getFluid().getAttributes().getColor(selectedFluid);
        this.hsv = Color.rgbToHSV((color >> 16) & 255, (color >> 8) & 255, color & 255);
    }

    @Override
    public void decodeUpdate(CompoundNBT tag)
    {
        super.decodeUpdate(tag);
        blockChanged();
    }

    @Override
    public void encode(CompoundNBT tag)
    {
        if (!selectedFluid.isEmpty())
            selectedFluid.writeToNBT(tag);
    }

    public boolean canSelectFluid(FluidStack stack)
    {
        return !stack.isFluidEqual(selectedFluid);
    }

    public FluidStack getSelectedFluid()
    {
        if (selectedFluid.isEmpty())
            return FluidStack.EMPTY;

        FluidStack stack = selectedFluid.copy();
        stack.setAmount(150 - level.getBestNeighborSignal(worldPosition) * 10);
        return stack;
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
