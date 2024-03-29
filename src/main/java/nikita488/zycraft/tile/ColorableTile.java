package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.util.Color;

public class ColorableTile extends ZYTile implements IColorable
{
    protected Color color;

    public ColorableTile(TileEntityType<?> type)
    {
        this(type, 0xFFFFFF);
    }

    public ColorableTile(TileEntityType<?> type, int defaultColor)
    {
        super(type);
        this.color = Color.fromRGB(defaultColor);
    }

    @Override
    public int getColor(BlockState state, IBlockDisplayReader getter, BlockPos pos)
    {
        return color.rgb();
    }

    @Override
    public void setColor(BlockState state, IBlockDisplayReader getter, BlockPos pos, int rgb)
    {
        this.color = Color.fromRGB(rgb);
        setChanged();
        sendUpdated();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);
        this.color = Color.load(tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);
        color.save(tag);
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.color = Color.load(tag);
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
