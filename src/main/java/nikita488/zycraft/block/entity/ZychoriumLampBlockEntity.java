package nikita488.zycraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.block.ZychoriumLampBlock;
import nikita488.zycraft.util.Color;

import javax.annotation.Nullable;

public class ZychoriumLampBlockEntity extends ColorableBlockEntity
{
    @Nullable
    private float[] hsv;

    public ZychoriumLampBlockEntity(BlockEntityType<ZychoriumLampBlockEntity> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    protected float brightness(boolean inverted)
    {
        int signal = level.getBestNeighborSignal(worldPosition);
        return 0.25F + 0.05F * (inverted ? 15 - signal : signal);
    }

    @Override
    public int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int tintIndex)
    {
        boolean inverted = ((ZychoriumLampBlock)state.getBlock()).inverted();
        int defaultColor = inverted ? 0xFFFFFF : 0x3F3F3F;

        return hsv != null ? Color.hsvToRGB(hsv[0], hsv[1], hsv[2] * brightness(inverted)) : defaultColor;
    }

    @Override
    public void decode(CompoundTag tag)
    {
        super.decode(tag);
        this.hsv = Color.rgbToHSV(color.red(), color.green(), color.blue());
    }
}
