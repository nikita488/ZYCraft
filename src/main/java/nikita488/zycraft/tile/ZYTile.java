package nikita488.zycraft.tile;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.util.BlockUtils;

import javax.annotation.Nullable;

public class ZYTile extends BlockEntity
{
    public ZYTile(BlockEntityType<?> type)
    {
        super(type);
    }

    public boolean stillValid(Player player)
    {
        return level.getBlockEntity(worldPosition) == this && player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64D;
    }

    public void updateNeighbourForOutputSignal()
    {
        BlockState state = getBlockState();

        if (!level.isClientSide() && state.hasAnalogOutputSignal())
            level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
    }

    public void sendUpdated()
    {
        BlockUtils.sendBlockUpdated(level, worldPosition, getBlockState());
    }

    public void blockChanged()
    {
        BlockUtils.blockChanged(level, worldPosition, getBlockState(), false);
    }

    public void decode(CompoundTag tag) {}

    public void encode(CompoundTag tag) {}

    @Override
    public CompoundTag getUpdateTag()
    {
        return Util.make(super.getUpdateTag(), this::encode);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag)
    {
        decode(tag);
    }

    public void decodeUpdate(CompoundTag tag)
    {
        decode(tag);
    }

    public void encodeUpdate(CompoundTag tag)
    {
        encode(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag tag = Util.make(new CompoundTag(), this::encodeUpdate);
        return !tag.isEmpty() ? new ClientboundBlockEntityDataPacket(worldPosition, 0, tag) : null;
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet)
    {
        decodeUpdate(packet.getTag());
    }
}
