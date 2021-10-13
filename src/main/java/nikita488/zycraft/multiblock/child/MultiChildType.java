package nikita488.zycraft.multiblock.child;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBiomeReader;
import net.minecraft.world.IBlockReader;
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
    public static MultiChildType get(IBlockReader world, BlockPos pos)
    {
        return get(world.getBlockState(pos), world, pos);
    }

    @Nullable
    public static MultiChildType get(BlockState state, IBlockReader world, BlockPos pos)
    {
        for (MultiChildType type : VALUES)
            if (type.matches(state, world, pos))
                return type;

        return null;
    }

    public static boolean convert(IBiomeReader world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        MultiChildType type = get(state, world, pos);

        if (type == null)
            return false;

        BlockState childState = type.block().defaultBlockState();

        if (childState.getBlock() instanceof ConvertedMultiChildBlock)
            childState = childState.setValue(ConvertedMultiChildBlock.USE_SHAPE_FOR_LIGHT_OCCLUSION, state.useShapeForLightOcclusion())
                    .setValue(ConvertedMultiChildBlock.HAS_ANALOG_OUTPUT_SIGNAL, state.hasAnalogOutputSignal())
                    .setValue(ConvertedMultiChildBlock.SIGNAL_SOURCE, state.isSignalSource());

        world.setBlock(pos, childState, Constants.BlockFlags.BLOCK_UPDATE);

        TileEntity tile = world.getBlockEntity(pos);

        if (!(tile instanceof IMultiChild))
            return false;

        if (tile instanceof ConvertedMultiChildTile)
            ((ConvertedMultiChildTile)tile).setInitialState(state);
        return true;
    }

    public Block block()
    {
        return blockSupplier.get();
    }

    @Override
    public boolean matches(IBlockReader world, BlockPos pos)
    {
        return get(world, pos) == this;
    }

    public boolean matches(BlockState state, IBlockReader world, BlockPos pos)
    {
        return (!state.hasTileEntity() || state.getBlock() instanceof ConvertedMultiChildBlock) && matcher.matches(state, world, pos);
    }

    @FunctionalInterface
    private interface Matcher
    {
        boolean matches(BlockState state, IBlockReader world, BlockPos pos);
    }
}
