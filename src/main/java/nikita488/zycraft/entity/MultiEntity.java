package nikita488.zycraft.entity;

import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import nikita488.zycraft.init.ZYEntities;
import nikita488.zycraft.multiblock.IDynamicMultiBlock;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.MultiManager;

import javax.annotation.Nullable;

public class MultiEntity extends Entity implements IEntityAdditionalSpawnData
{
    @Nullable
    private IDynamicMultiBlock parentMultiBlock;
    private int multiID = -1;

    public MultiEntity(EntityType<MultiEntity> type, World world)
    {
        super(type, world);
    }

    public MultiEntity(World world, IDynamicMultiBlock parentMultiBlock, int multiID)
    {
        super(ZYEntities.MULTI_BLOCK.get(), world);

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
        if (!level.isClientSide() || multiID < 0)
            return;

        MultiBlock multiBlock = MultiManager.getInstance(level).getMultiBlock(multiID);

        if (multiBlock instanceof IDynamicMultiBlock)
        {
            this.parentMultiBlock = (IDynamicMultiBlock)multiBlock;
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
                remove();

        this.firstTick = false;
    }

    @Override
    public void setPos(double x, double y, double z)
    {
        setPosRaw(x, y, z);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.multiID = buffer.readVarInt();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeVarInt(multiID);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundNBT tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundNBT tag) {}

    @Override
    public IPacket<?> getAddEntityPacket()
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
    public void thunderHit(ServerWorld world, LightningBoltEntity lightning) {}

    @Nullable
    public IDynamicMultiBlock parentMultiBlock()
    {
        return parentMultiBlock;
    }
}
