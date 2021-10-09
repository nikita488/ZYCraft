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
    private IDynamicMultiBlock multiBlock;
    private int multiID = -1;

    public MultiEntity(EntityType<MultiEntity> type, World world)
    {
        super(type, world);
    }

    public MultiEntity(World world, IDynamicMultiBlock multiBlock, int multiID)
    {
        super(ZYEntities.MULTI_BLOCK.get(), world);

        this.multiBlock = multiBlock;
        this.multiID = multiID;
        this.noClip = true;
        this.preventEntitySpawning = true;

        BlockPos originPos = multiBlock.origin();
        setPosition(originPos.getX(), originPos.getY(), originPos.getZ());
        setBoundingBox(multiBlock.aabb());
    }

    private void updateMultiBlock()
    {
        if (!world.isRemote() || multiID < 0)
            return;

        MultiBlock multiBlock = MultiManager.getInstance(world).getMultiBlock(multiID);

        if (multiBlock instanceof IDynamicMultiBlock)
        {
            this.multiBlock = (IDynamicMultiBlock)multiBlock;
            setBoundingBox(this.multiBlock.aabb());
            this.multiID = -1;
        }
    }

    @Override
    public void tick()
    {
        updateMultiBlock();

        if (multiBlock != null)
            if (multiBlock.isValid())
                multiBlock.tick();
            else
                remove();

        this.firstUpdate = false;
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        setRawPosition(x, y, z);
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
    protected void registerData() {}

    @Override
    protected void readAdditional(CompoundNBT tag) {}

    @Override
    protected void writeAdditional(CompoundNBT tag) {}

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public PushReaction getPushReaction()
    {
        return PushReaction.IGNORE;
    }

    @Override
    public void onKillCommand() {}

    @Override
    public void causeLightningStrike(ServerWorld world, LightningBoltEntity lightning) {}

    @Nullable
    public IDynamicMultiBlock getMultiBlock()
    {
        return multiBlock;
    }
}
