package nikita488.zycraft.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.multiblock.child.DefaultMultiChildBlock;
import nikita488.zycraft.multiblock.block.MultiChildBlock;
import nikita488.zycraft.multiblock.tile.DefaultMultiChildTile;

public enum MultiChildType implements IStringSerializable
{
    AIR("air", Material.AIR),
    HARD("hard", Material.ROCK),
    GLASS("glass", Material.GLASS);

    private final String name;
    private final Material material;
    public static final MultiChildType[] VALUES = values();

    MultiChildType(String name, Material material)
    {
        this.name = name;
        this.material = material;
    }

    public static boolean tryConvertBlock(World world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        MultiChildType type = MultiChildType.fromState(state, world, pos);

        if (type == null)
            return false;

        BlockState childState = ZYBlocks.DEFAULT_MULTI_CHILD.get(type).getDefaultState()
                .with(DefaultMultiChildBlock.TICKS_RANDOMLY, state.ticksRandomly())
                .with(DefaultMultiChildBlock.TRANSPARENT, state.isTransparent())
                .with(DefaultMultiChildBlock.PROVIDES_POWER, state.canProvidePower())
                .with(DefaultMultiChildBlock.OVERRIDES_INPUT, state.hasComparatorInputOverride());

        world.setBlockState(pos, childState, Constants.BlockFlags.BLOCK_UPDATE);

        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof DefaultMultiChildTile))
            return false;

        ((DefaultMultiChildTile)tile).setState(state);
        return true;
    }

    public static MultiChildType fromState(IBlockReader world, BlockPos pos)
    {
        return fromState(world.getBlockState(pos), world, pos);
    }

    private static boolean isMaterialSolid(Material material)
    {
        return material.isSolid() && material.blocksMovement() && !material.isReplaceable() && !material.isLiquid();
    }

    public static MultiChildType fromState(BlockState state, IBlockReader world, BlockPos pos)
    {
        Material material = state.getMaterial();

        if (!(state.getBlock() instanceof MultiChildBlock) && state.hasTileEntity())
            return null;

        if (state.isAir(world, pos) || state.isAir())
            return AIR;

        if (!isMaterialSolid(material) || !state.hasOpaqueCollisionShape(world, pos))
            return null;

        if (!state.isOpaqueCube(world, pos))
            return material == Material.GLASS ? GLASS : null;

        if (state.getRequiresTool())
            return HARD;

        return null;
    }

    public boolean isAir()
    {
        return this == AIR;
    }

    public boolean isGlass()
    {
        return this == GLASS;
    }

    @Override
    public String getString()
    {
        return name;
    }

    public Material material()
    {
        return material;
    }
}
