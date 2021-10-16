package nikita488.zycraft.multiblock.child;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.multiblock.child.block.ConvertedMultiChildBlock;
import nikita488.zycraft.multiblock.child.tile.ConvertedMultiChildTile;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.Arrays;

public enum MultiChildType implements IMultiChildMatcher
{
    AIR(ZYBlocks.MULTI_AIR, (state, world, pos) -> state.isAir() || state.isAir(world, pos)),
    FLAMMABLE(ZYBlocks.FLAMMABLE_BLOCK, (state, world, pos) -> state.isCollisionShapeFullBlock(world, pos) && state.isSolidRender(world, pos) &&
            Arrays.stream(ZYConstants.DIRECTIONS).anyMatch(side -> state.isFlammable(world, pos, side))),
    HARD(ZYBlocks.HARD_BLOCK, (state, world, pos) -> state.isCollisionShapeFullBlock(world, pos) && state.isSolidRender(world, pos) &&
            state.requiresCorrectToolForDrops()),
    GLASS(ZYBlocks.GLASS_BLOCK, (state, world, pos) -> state.isCollisionShapeFullBlock(world, pos) && !state.isSolidRender(world, pos) &&
            (state.getMaterial() == Material.GLASS || state.getMaterial() == Material.BUILDABLE_GLASS));

    public static final MultiChildType[] VALUES = values();
    private final NonNullSupplier<? extends Block> blockSupplier;
    private final Matcher matcher;

    MultiChildType(NonNullSupplier<? extends Block> blockSupplier, Matcher matcher)
    {
        this.blockSupplier = blockSupplier;
        this.matcher = matcher;
    }

    @Nullable
    public static MultiChildType get(BlockGetter getter, BlockPos pos)
    {
        return get(getter.getBlockState(pos), getter, pos);
    }

    @Nullable
    public static MultiChildType get(BlockState state, BlockGetter getter, BlockPos pos)
    {
        for (MultiChildType type : VALUES)
            if (type.matches(state, getter, pos))
                return type;

        return null;
    }

    public static boolean convert(CommonLevelAccessor accessor, BlockPos pos)
    {
        BlockState state = accessor.getBlockState(pos);
        MultiChildType type = get(state, accessor, pos);

        if (type == null)
            return false;

        BlockState childState = type.block().defaultBlockState();

        if (childState.getBlock() instanceof ConvertedMultiChildBlock)
            childState = childState.setValue(ConvertedMultiChildBlock.USE_SHAPE_FOR_LIGHT_OCCLUSION, state.useShapeForLightOcclusion())
                    .setValue(ConvertedMultiChildBlock.HAS_ANALOG_OUTPUT_SIGNAL, state.hasAnalogOutputSignal())
                    .setValue(ConvertedMultiChildBlock.SIGNAL_SOURCE, state.isSignalSource());

        accessor.setBlock(pos, childState, Constants.BlockFlags.BLOCK_UPDATE);

        BlockEntity blockEntity = accessor.getBlockEntity(pos);

        if (!(blockEntity instanceof IMultiChild))
            return false;

        if (blockEntity instanceof ConvertedMultiChildTile)
            ((ConvertedMultiChildTile)blockEntity).setInitialState(state);
        return true;
    }

    public Block block()
    {
        return blockSupplier.get();
    }

    @Override
    public boolean matches(BlockGetter getter, BlockPos pos)
    {
        return get(getter, pos) == this;
    }

    public boolean matches(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return (!state.hasTileEntity() || state.getBlock() instanceof ConvertedMultiChildBlock) && matcher.matches(state, getter, pos);
    }

    @FunctionalInterface
    private interface Matcher
    {
        boolean matches(BlockState state, BlockGetter getter, BlockPos pos);
    }
}
