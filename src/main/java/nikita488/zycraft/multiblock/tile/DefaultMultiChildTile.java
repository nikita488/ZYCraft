package nikita488.zycraft.multiblock.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.util.BlockUtils;

import javax.annotation.Nonnull;

public class DefaultMultiChildTile extends MultiChildTile
{
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();
    public static ModelProperty<BlockPos> POS = new ModelProperty<>();
    private BlockState state = Blocks.AIR.getDefaultState();

    public DefaultMultiChildTile(TileEntityType<DefaultMultiChildTile> type)
    {
        super(type);
    }

    public void setState(BlockState newState)
    {
        if (state == newState)
            return;

        this.state = newState;
        BlockUtils.scheduleTileUpdate(world, pos, getBlockState());
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(STATE, state)
                .withInitial(POS, pos)
                .build();
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!world.isRemote && !hasParent())
            world.setBlockState(pos, state);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);
        this.state = NBTUtil.readBlockState(compound.getCompound("State"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.put("State", NBTUtil.writeBlockState(state));
        return compound;
    }

    @Override
    public void decode(CompoundNBT compound)
    {
        this.state = Block.getStateById(compound.getInt("State"));
        //requestModelDataUpdate();
    }

    @Override
    public void encode(CompoundNBT compound)
    {
        compound.putInt("State", Block.getStateId(state));
    }

    public BlockState state()
    {
        return state;
    }
}
