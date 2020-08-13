package nikita488.zycraft.multiblock.test;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.init.ZYMultiBlocks;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;

public class TestMultiBlock extends MultiBlock
{
    private BlockPos insidePos;

    public TestMultiBlock()
    {
        super(ZYMultiBlocks.TEST.get());
    }

    public TestMultiBlock(IWorld parentWorld, ChunkPos parentChunk, BlockPos insidePos)
    {
        super(ZYMultiBlocks.TEST.get(), parentWorld, parentChunk);
        this.insidePos = insidePos;
    }

    @Override
    public void initChildBlocks()
    {
        addChildBlocks(insidePos.getX() - 1, insidePos.getY() - 1, insidePos.getZ() - 1, insidePos.getX() + 1, insidePos.getY() + 1, insidePos.getZ() + 1);
    }

    @Override
    public void onChildValidation(MultiChildTile child, MultiValidationType type)
    {

    }

    @Override
    public void onChildInvalidation(MultiChildTile child, MultiInvalidationType type)
    {

    }

    @Override
    public ActionResultType onChildInteraction(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        return ActionResultType.PASS;
    }

    @Override
    public int getChildLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return 15;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBlockPos(insidePos);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        insidePos = buffer.readBlockPos();
    }

    @Override
    public void encodeUpdate(PacketBuffer buffer) {}

    @Override
    public void decodeUpdate(PacketBuffer buffer) {}

    @Override
    public void save(CompoundNBT tag)
    {
        tag.put("InsidePos", NBTUtil.writeBlockPos(insidePos));
    }

    @Override
    public void load(CompoundNBT tag)
    {
        insidePos = NBTUtil.readBlockPos(tag.getCompound("InsidePos"));
    }
}
