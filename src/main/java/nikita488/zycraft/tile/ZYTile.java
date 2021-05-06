package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import nikita488.zycraft.util.BlockUtils;

import javax.annotation.Nullable;

public class ZYTile extends TileEntity
{
    public ZYTile(TileEntityType<?> type)
    {
        super(type);
    }

    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64;
    }

    public void sendUpdated()
    {
        BlockUtils.sendBlockUpdated(world, pos, getBlockState());
    }

    public void decode(CompoundNBT tag) {}

    public void encode(CompoundNBT tag) {}

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT tag = super.getUpdateTag();
        encode(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        decode(tag);
    }

    public void decodeUpdate(CompoundNBT tag)
    {
        decode(tag);
    }

    public void encodeUpdate(CompoundNBT tag)
    {
        encode(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT tag = new CompoundNBT();
        encodeUpdate(tag);
        return new SUpdateTileEntityPacket(pos, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet)
    {
        decodeUpdate(packet.getNbtCompound());
    }
}
