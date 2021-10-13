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
    private BlockState initialState = Blocks.AIR.defaultBlockState();

    public ConvertedMultiChildTile(TileEntityType<ConvertedMultiChildTile> type)
    {
        super(type);
    }

    public void setInitialState(BlockState state)
    {
        if (state == initialState)
            return;

        this.initialState = state;
        setChanged();
        sendUpdated();
    }

    @Nonnull
    @Override
    public IModelData getModelData()
    {
        return new ModelDataMap.Builder()
                .withInitial(INITIAL_STATE, initialState)
                .withInitial(BLOCK_GETTER, new BlockGetter())
                .withInitial(POS, worldPosition)
                .build();
    }

    @Override
    public void onMultiInvalidation(MultiBlock multiBlock)
    {
        super.onMultiInvalidation(multiBlock);

        if (!level.isClientSide() && !hasParents())
            level.setBlock(worldPosition, initialState, Constants.BlockFlags.BLOCK_UPDATE);
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
    public void load(BlockState state, CompoundNBT tag)
    {
        super.load(state, tag);
        this.initialState = NBTUtil.readBlockState(tag.getCompound("InitialState"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag)
    {
        super.save(tag);
        tag.put("InitialState", NBTUtil.writeBlockState(initialState));
        return tag;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        this.initialState = Block.stateById(tag.getInt("InitialState"));
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
        tag.putInt("InitialState", Block.getId(initialState));
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
            if (pos.equals(ConvertedMultiChildTile.this.getBlockPos()))
                return initialState;

            TileEntity tile = getBlockEntity(pos);
            return tile instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)tile).initialState() : level.getBlockState(pos);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public float getShade(Direction side, boolean applyDiffuseLighting)
        {
            return level.getShade(side, applyDiffuseLighting);
        }

        @Override
        public WorldLightManager getLightEngine()
        {
            return level.getLightEngine();
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public int getBlockTint(BlockPos pos, ColorResolver resolver)
        {
            return level.getBlockTint(pos, resolver);
        }

        @Nullable
        @Override
        public TileEntity getBlockEntity(BlockPos pos)
        {
            return level.getBlockEntity(pos);
        }

        @Override
        public FluidState getFluidState(BlockPos pos)
        {
            return level.getFluidState(pos);
        }
    }
}
