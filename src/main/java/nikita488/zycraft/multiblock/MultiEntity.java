package nikita488.zycraft.multiblock;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYEntities;

import javax.annotation.Nonnull;

public class MultiEntity extends Entity implements IEntityAdditionalSpawnData
{
    private MultiBlock multiBlock;
    private int multiID = -1;

    public MultiEntity(EntityType<MultiEntity> type, World world)
    {
        super(type, world);
    }

    public MultiEntity(@Nonnull MultiBlock multiBlock)
    {
        super(ZYEntities.MULTI_BLOCK.get(), multiBlock.world());
        this.multiBlock = multiBlock;
        this.noClip = true;
        canUpdate(true);
    }

    @Override
    public void tick()
    {
        MultiBlock multiBlock = getMultiBlock();

        if (multiBlock != null)
        {
            if (!multiBlock.isValid())
                remove();

            if (!removed)
                multiBlock.tick();
        }

        this.firstUpdate = false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        MultiBlock multiBlock = getMultiBlock();
        return multiBlock != null ? multiBlock.getRenderBoundingBox() : super.getRenderBoundingBox();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        MultiBlock multiBlock = getMultiBlock();
        return multiBlock != null ? multiBlock.isInRangeToRenderDist(distance) : super.isInRangeToRenderDist(distance);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        setRawPosition(x, y, z);
        setBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1));
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.multiID = buffer.readVarInt();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeVarInt(multiBlock.id());
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

    public MultiBlock getMultiBlock()
    {
        if (multiBlock == null && multiID != -1)
        {
            this.multiBlock = MultiManager.getInstance(world).getMultiBlock(multiID);
            canUpdate(true);
        }
        return multiBlock;
    }
}
