package com.nikita488.zycraft.tile;

import com.nikita488.zycraft.util.BlockUpdate;
import com.nikita488.zycraft.util.Color4b;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class ColorableTile extends BaseTile
{
    protected Color4b color = Color4b.from(0xFFFFFFFF);

    public ColorableTile(TileEntityType<? extends ColorableTile> type)
    {
        super(type);
    }

    public void addRGB(int r, int g, int b)
    {
        color.add(r, g, b, 0);
        BlockUpdate.scheduleTileUpdate(world, pos, getBlockState());
    }

    public void subtractRGB(int r, int g, int b)
    {
        addRGB(-r, -g, -b);
    }

    public void setRGB(int rgb)
    {
        color.set(rgb, 255);
        BlockUpdate.scheduleTileUpdate(world, pos, getBlockState());
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        color = Color4b.loadRGB(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        color.saveRGB(compound);
        return compound;
    }

    @Override
    public void decode(CompoundNBT compound)
    {
        color = Color4b.loadRGB(compound);
    }

    @Override
    public void decodeUpdate(CompoundNBT compound)
    {
        color = Color4b.loadRGB(compound);
        BlockUpdate.scheduleRenderUpdate(world, pos);
    }

    @Override
    public void encode(CompoundNBT compound)
    {
        color.saveRGB(compound);
    }

    @Override
    public void encodeUpdate(CompoundNBT compound)
    {
        color.saveRGB(compound);
    }

    public Color4b color()
    {
        return color;
    }
}
