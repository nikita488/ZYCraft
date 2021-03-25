package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import nikita488.zycraft.util.BlockUtils;
import nikita488.zycraft.util.Color4b;

public class ColorableTile extends ZYTile
{
    protected Color4b color;

    public ColorableTile(TileEntityType<? extends ColorableTile> type)
    {
        this(type, 0xFFFFFF);
    }

    public ColorableTile(TileEntityType<? extends ColorableTile> type, int defaultColor)
    {
        super(type);
        color = Color4b.from(defaultColor, 255);
    }

    public void setRGB(int rgb)
    {
        color.set(rgb, 255);
        BlockUtils.sendBlockUpdated(level, worldPosition, getBlockState());
    }

    @Override
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);
        color = Color4b.loadRGB(tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);
        color.saveRGB(tag);
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        color = Color4b.loadRGB(tag);
    }

    @Override
    public void decodeUpdate(CompoundNBT tag)
    {
        color = Color4b.loadRGB(tag);
        BlockUtils.sendBlockUpdated(level, worldPosition, getBlockState());
    }

    @Override
    public void encode(CompoundNBT tag)
    {
        color.saveRGB(tag);
    }

    @Override
    public void encodeUpdate(CompoundNBT tag)
    {
        color.saveRGB(tag);
    }

    public Color4b color()
    {
        return color;
    }

    public int rgb()
    {
        return color.rgb();
    }
}
