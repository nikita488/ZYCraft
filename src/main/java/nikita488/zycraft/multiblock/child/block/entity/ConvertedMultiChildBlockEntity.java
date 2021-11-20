package nikita488.zycraft.multiblock.child.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import nikita488.zycraft.multiblock.MultiBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConvertedMultiChildBlockEntity extends MultiChildBlockEntity
{
    public static final ModelProperty<BlockState> INITIAL_STATE = new ModelProperty<>();
    public static final ModelProperty<BlockAndTintGetter> BLOCK_GETTER = new ModelProperty<>();
    public static final ModelProperty<BlockPos> POS = new ModelProperty<>();
    private BlockState initialState = Blocks.AIR.defaultBlockState();

    public ConvertedMultiChildBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
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
            level.setBlock(worldPosition, initialState, Block.UPDATE_CLIENTS);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.initialState = NbtUtils.readBlockState(tag.getCompound("InitialState"));
    }

    @Override
    public CompoundTag save(CompoundTag tag)
    {
        super.save(tag);
        tag.put("InitialState", NbtUtils.writeBlockState(initialState));
        return tag;
    }

    @Override
    public void decode(CompoundTag tag)
    {
        this.initialState = Block.stateById(tag.getInt("InitialState"));
    }

    @Override
    public void decodeUpdate(CompoundTag tag)
    {
        super.decodeUpdate(tag);
        requestModelDataUpdate();
        blockChanged();
    }

    @Override
    public void encode(CompoundTag tag)
    {
        tag.putInt("InitialState", Block.getId(initialState));
    }

    public BlockState initialState()
    {
        return initialState;
    }

    private class BlockGetter implements BlockAndTintGetter
    {
        @Override
        public BlockState getBlockState(BlockPos pos)
        {
            if (pos.equals(ConvertedMultiChildBlockEntity.this.getBlockPos()))
                return initialState;

            return getBlockEntity(pos) instanceof ConvertedMultiChildBlockEntity converted ? converted.initialState() : level.getBlockState(pos);
        }

        @Override
        public float getShade(Direction side, boolean applyDiffuseLighting)
        {
            return level.getShade(side, applyDiffuseLighting);
        }

        @Override
        public LevelLightEngine getLightEngine()
        {
            return level.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver resolver)
        {
            return level.getBlockTint(pos, resolver);
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pos)
        {
            return level.getBlockEntity(pos);
        }

        @Override
        public FluidState getFluidState(BlockPos pos)
        {
            return level.getFluidState(pos);
        }

        @Override
        public int getHeight()
        {
            return level.getHeight();
        }

        @Override
        public int getMinBuildHeight()
        {
            return level.getMinBuildHeight();
        }
    }
}
