package nikita488.zycraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.util.Color;

public class ColorableBlockEntity extends ZYBlockEntity implements IColorable
{
    protected Color color;

    public ColorableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        this(type, pos, state, 0xFFFFFF);
    }

    public ColorableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int defaultColor)
    {
        super(type, pos, state);
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
    public void load(CompoundTag tag)
    {
        super.load(tag);
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
