package nikita488.zycraft.multiblock.child.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
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
    public static final ModelProperty<BlockAndTintGetter> BLOCK_GETTER = new ModelProperty<>();
    public static final ModelProperty<BlockPos> POS = new ModelProperty<>();
    private BlockState initialState = Blocks.AIR.defaultBlockState();

    public ConvertedMultiChildTile(BlockEntityType<ConvertedMultiChildTile> type)
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
    public void load(BlockState state, CompoundTag tag)
    {
        super.load(state, tag);
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
            if (pos.equals(ConvertedMultiChildTile.this.getBlockPos()))
                return initialState;

            BlockEntity blockEntity = getBlockEntity(pos);
            return blockEntity instanceof ConvertedMultiChildTile ? ((ConvertedMultiChildTile)blockEntity).initialState() : level.getBlockState(pos);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
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
        @OnlyIn(Dist.CLIENT)
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
    }
}
