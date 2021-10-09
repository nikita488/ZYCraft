package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.multiblock.MultiBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConvertedMultiChildTile extends MultiChildTile
{
    public static final ModelProperty<BlockState> INITIAL_STATE = new ModelProperty<>();
    public static final ModelProperty<IBlockDisplayReader> BLOCK_GETTER = new ModelProperty<>();
    public static final ModelProperty<BlockPos> POS = new ModelProperty<>();
    private BlockState initialState = Blocks.AIR.getDefaultState();

    public ConvertedMultiChildTile(TileEntityType<ConvertedMultiChildTile> type)
    {
        super(type);
    }

    public void setInitialState(BlockState state)
    {
        if (state == initialState)
            return;

        this.initialState = state;
        markDirty();
        sendUpdated();
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(INITIAL_STATE, initialState)
                .withInitial(BLOCK_GETTER, new BlockGetter())
                .withInitial(POS, pos)
                .build();
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!world.isRemote() && !hasParents())
            world.setBlockState(pos, initialState, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void mirror(Mirror mirror)
    {
        setInitialState(initialState.mirror(mirror));
    }

    @Override
    public void rotate(Rotation rotation)
    {
        setInitialState(initialState.rotate(rotation));
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        this.initialState = NBTUtil.readBlockState(tag.getCompound("InitialState"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        tag.put("InitialState", NBTUtil.writeBlockState(initialState));
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.initialState = Block.getStateById(tag.getInt("InitialState"));
    }

    @Override
    public void decodeUpdate(CompoundNBT tag)
    {
        super.decodeUpdate(tag);
        requestModelDataUpdate();
        blockChanged();
    }

    @Override
    public void encode(CompoundNBT tag)
    {
        tag.putInt("InitialState", Block.getStateId(initialState));
    }

    public BlockState initialState()
    {
        return initialState;
    }

    private class BlockGetter implements IBlockDisplayReader
    {
        @Override
        public BlockState getBlockState(BlockPos pos)
        {
            if (pos.equals(ConvertedMultiChildTile.this.getPos()))
                return initialState;

            TileEntity tile = getTileEntity(pos);
            return tile instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)tile).initialState() : world.getBlockState(pos);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public float func_230487_a_(Direction side, boolean applyDiffuseLighting)
        {
            return world.func_230487_a_(side, applyDiffuseLighting);
        }

        @Override
        public WorldLightManager getLightManager()
        {
            return world.getLightManager();
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getBlockColor(BlockPos pos, ColorResolver resolver)
        {
            return world.getBlockColor(pos, resolver);
        }

        @Nullable
        @Override
        public TileEntity getTileEntity(BlockPos pos)
        {
            return world.getTileEntity(pos);
        }

        @Override
        public FluidState getFluidState(BlockPos pos)
        {
            return world.getFluidState(pos);
        }
    }
}
