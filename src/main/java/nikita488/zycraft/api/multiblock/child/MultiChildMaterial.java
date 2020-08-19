package nikita488.zycraft.api.multiblock.child;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public enum MultiChildMaterial implements IStringSerializable
{
    AIR("air", Material.AIR),
    SOFT("soft", Material.EARTH),
    HARD("hard", Material.ROCK),
    FLAMMABLE("flammable", Material.WOOD),
    GLASS("glass", Material.GLASS),
    NON_OPAQUE("non_opaque", Material.GLASS);

    private final String name;
    private final Material material;
    public static final MultiChildMaterial[] VALUES = values();

    MultiChildMaterial(String name, Material material)
    {
        this.name = name;
        this.material = material;
    }

    public static MultiChildMaterial fromState(IBlockReader world, BlockPos pos)
    {
        return fromState(world.getBlockState(pos), world, pos);
    }

    public static MultiChildMaterial fromState(BlockState state, IBlockReader world, BlockPos pos)
    {
        Material material = state.getMaterial();

        if (!(state.getBlock() instanceof MultiChildBlock) && state.hasTileEntity())
            return null;

        if (state.isAir(world, pos))
            return AIR;

        if (material.isLiquid() || !material.blocksMovement() || material.isReplaceable() || !material.isSolid())
            return null;

        if (material.getPushReaction() == PushReaction.DESTROY || material.getPushReaction() == PushReaction.BLOCK)
            return null;

        if (!state.isCollisionShapeOpaque(world, pos))
            return null;

        if (!state.isOpaqueCube(world, pos) || !material.isOpaque())
            return material == Material.GLASS ? GLASS : NON_OPAQUE;

        if (material != Material.SNOW_BLOCK && !material.isToolNotRequired())
            return HARD;

        if (material.isFlammable())
            return FLAMMABLE;

        return SOFT;
    }

    public boolean notSolid()
    {
        //TODO: Make decision about air
        return this == AIR || this == GLASS || this == NON_OPAQUE;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public Material material()
    {
        return material;
    }
}
