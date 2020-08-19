package nikita488.zycraft.multiblock.child;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import nikita488.zycraft.api.multiblock.MultiBlock;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;
import nikita488.zycraft.api.multiblock.MultiInvalidationType;

import javax.annotation.Nonnull;

public class DefaultMultiChildTile extends MultiChildTile
{
    public static ModelProperty<BlockState> STATE = new ModelProperty<>();
    private BlockState state = Blocks.AIR.getDefaultState();

    public DefaultMultiChildTile(TileEntityType<DefaultMultiChildTile> type)
    {
        super(type);
    }

    public void setState(BlockState state)
    {
        this.state = state;
        //BlockUpdate.scheduleTileUpdate(world, pos, getBlockState());
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder().withInitial(STATE, state).build();
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock, MultiInvalidationType type)
    {
        super.onMultiInvalidation(multiBlock, type);

        if (!world.isRemote && parentMultiBlocks.isEmpty())
            world.setBlockState(pos, state);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        state = NBTUtil.readBlockState(compound.getCompound("State"));
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
        state = Block.getStateById(compound.getInt("State"));
    }

    @Override
    public void encode(CompoundNBT compound)
    {
        compound.putInt("State", Block.getStateId(state));
    }

    @Override
    public void decodeUpdate(CompoundNBT compound)
    {
        decode(compound);
        //BlockUpdate.scheduleRenderUpdate(world, pos);
    }

    @Override
    public void encodeUpdate(CompoundNBT compound)
    {
        encode(compound);
    }

    public BlockState state()
    {
        return state;
    }
}
