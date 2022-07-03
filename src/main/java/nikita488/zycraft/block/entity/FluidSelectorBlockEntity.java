package nikita488.zycraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;
import nikita488.zycraft.util.Color;

import javax.annotation.Nullable;

public class FluidSelectorBlockEntity extends ZYBlockEntity
{
    @Nullable
    private float[] hsv;
    private FluidStack selectedFluid = FluidStack.EMPTY;

    public FluidSelectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    public int getColor()
    {
        float brightness = (15 - level.getBestNeighborSignal(worldPosition)) / 15F;
        return hsv != null ? Color.hsvToRGB(hsv[0], hsv[1], hsv[2] * brightness) : 0xFFFFFF;
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        if (tag.contains("SelectedFluid", Tag.TAG_COMPOUND))
            this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("SelectedFluid"));
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        if (!selectedFluid.isEmpty())
            tag.put("SelectedFluid", selectedFluid.writeToNBT(new CompoundTag()));
    }

    @Override
    public void decode(CompoundTag tag)
    {
        this.selectedFluid = FluidStack.loadFluidStackFromNBT(tag);

        if (selectedFluid.isEmpty())
            return;

        int color = RenderProperties.get(selectedFluid.getFluid()).getColorTint(selectedFluid);
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
