package com.nikita488.zycraft.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public class BaseTile extends TileEntity
{
    public BaseTile(TileEntityType<?> type)
    {
        super(type);
    }

    public void decode(CompoundNBT compound) {}

    public void encode(CompoundNBT compound) {}

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        encode(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT compound)
    {
        decode(compound);
    }

    public void decodeUpdate(CompoundNBT compound) {}

    public void encodeUpdate(CompoundNBT compound) {}

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT compound = new CompoundNBT();
        encodeUpdate(compound);
        return new SUpdateTileEntityPacket(pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        decodeUpdate(pkt.getNbtCompound());
    }
}
