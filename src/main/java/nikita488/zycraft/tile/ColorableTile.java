package nikita488.zycraft.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.util.Color;

public class ColorableTile extends ZYTile implements IColorable
{
    protected Color color;

    public ColorableTile(BlockEntityType<?> type)
    {
        this(type, 0xFFFFFF);
    }

    public ColorableTile(BlockEntityType<?> type, int defaultColor)
    {
        super(type);
        this.color = Color.fromRGB(defaultColor);
    }

    @Override
    public int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos)
    {
        return color.rgb();
    }

    @Override
    public void setColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int rgb)
    {
        this.color = Color.fromRGB(rgb);
        setChanged();
        sendUpdated();
    }

    @Override
    public void load(BlockState state, CompoundTag tag)
    {
        super.load(state, tag);
        this.color = Color.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        super.save(tag);
        color.save(tag);
        return tag;
    }

    @Override
    public void decode(CompoundTag tag)
    {
        this.color = Color.load(tag);
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
        color.save(tag);
    }

    public Color color()
    {
        return color;
    }

    public int rgb()
    {
        return color.rgb();
    }
}
