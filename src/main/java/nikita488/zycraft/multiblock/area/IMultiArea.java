package nikita488.zycraft.multiblock.area;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.util.Cuboid6i;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;

public interface IMultiArea
{
    @Nullable
    default Cuboid6i getArea(World level, BlockPos basePos, Direction formingSide)
    {
        BlockPos innerPos = basePos.relative(formingSide.getOpposite());

        if (!matches(level, innerPos))
            return null;

        Cuboid6i innerArea = new Cuboid6i.Mutable(innerPos);

        for (Direction side : ZYConstants.DIRECTIONS)
            if (canExpand(side, formingSide))
                while (innerArea.size(side.getAxis()) < maxSize(side) && matches(level, innerArea.copy().expand(side).side(side)))
                    innerArea.expand(side);

        return innerArea.immutable();
    }

    default boolean canExpand(Direction side, Direction formingSide)
    {
        return side != formingSide;
    }

    int maxSize(Direction side);

    default boolean matches(World level, Cuboid6i cuboid)
    {
        for (BlockPos pos : cuboid)
            if (!matches(level, pos))
                return false;
        return true;
    }

    boolean matches(World level, BlockPos pos);
}
