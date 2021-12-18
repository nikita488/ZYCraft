package nikita488.zycraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import nikita488.zycraft.init.ZYEntities;
import nikita488.zycraft.multiblock.IDynamicMultiBlock;
import nikita488.zycraft.multiblock.MultiManager;

import javax.annotation.Nullable;

public class MultiEntity extends Entity implements IEntityAdditionalSpawnData
{
    @Nullable
    private IDynamicMultiBlock parentMultiBlock;
    private int multiID = -1;

    public MultiEntity(EntityType<MultiEntity> type, Level level)
    {
        super(type, level);
    }

    public MultiEntity(Level level, IDynamicMultiBlock parentMultiBlock, int multiID)
    {
        super(ZYEntities.MULTI_BLOCK.get(), level);

        this.parentMultiBlock = parentMultiBlock;
        this.multiID = multiID;
        this.noPhysics = true;
        this.blocksBuilding = true;

        BlockPos originPos = parentMultiBlock.origin();
        setPos(originPos.getX(), originPos.getY(), originPos.getZ());
        setBoundingBox(parentMultiBlock.aabb());
    }

    private void computeClientMultiBlock()
    {
        if (level.isClientSide() && multiID >= 0 && MultiManager.getInstance(level).getMultiBlock(multiID) instanceof IDynamicMultiBlock multiBlock)
        {
            this.parentMultiBlock = multiBlock;
            setBoundingBox(parentMultiBlock.aabb());
            this.multiID = -1;
        }
    }

    @Override
    public void tick()
    {
        computeClientMultiBlock();

        if (parentMultiBlock != null)
            if (parentMultiBlock.isValid())
                parentMultiBlock.tick();
            else
                discard();

        this.firstTick = false;
    }

    @Override
    public void setPos(double x, double y, double z)
    {
        setPosRaw(x, y, z);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer)
    {
        this.multiID = buffer.readVarInt();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(multiID);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public PushReaction getPistonPushReaction()
    {
        return PushReaction.IGNORE;
    }

    @Override
    public void kill() {}

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {}

    @Nullable
    public IDynamicMultiBlock parentMultiBlock()
    {
        return parentMultiBlock;
    }
}
