package nikita488.zycraft.multiblock.tank;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikita488.zycraft.multiblock.*;
import nikita488.zycraft.multiblock.child.MultiChildMatcher;

public class TankFormer implements IMultiFormer
{
    public static final TankFormer INSTANCE = new TankFormer();
    private static final Direction[] VALUES = Direction.values();
    private static final MultiChildMatcher FACE = MultiChildMatcher.HARD_AND_GLASS;
    private static final MultiChildMatcher HARD = MultiChildMatcher.of(MultiChildType.HARD);

    @Override
    public boolean form(World world, BlockPos pos)
    {
        for (Direction side : VALUES)
        {
            BlockPos adjPos = pos.offset(side);

            if (!world.isAirBlock(adjPos))
                continue;

            Cuboid6i cuboid = new Cuboid6i(adjPos);

            for (Direction expandSide : VALUES)
                if (expandSide != side.getOpposite())
                    while (cuboid.length(expandSide.getAxis()) < 10 && isAir(world, cuboid.expand(expandSide).side(expandSide)))
                        cuboid = cuboid.expand(expandSide);

            BlockPos cornerPos = cuboid.min().add(-1, -1, -1);
            MultiPattern pattern = new MultiPattern(cuboid.lengthX() + 2, cuboid.lengthY() + 2, cuboid.lengthZ() + 2, new IMultiChildMatcher[]{HARD, FACE});

            pattern.setFrameMatcher(0, 0, 0, pattern.width() - 1, pattern.height() - 1, pattern.depth() - 1, 0);
            pattern.setFacesMatcher(0, 0, 0, pattern.width() - 1, pattern.height() - 1, pattern.depth() - 1, 1);
            pattern.setCuboidMatcher(1, 1, 1, pattern.width() - 2, pattern.height() - 2, pattern.depth() - 2, -1);

            if (!pattern.matches(world, cornerPos))
                continue;

            TankMultiBlock tank = new TankMultiBlock(world, cuboid);

            pattern.addChildBlocks(tank, cornerPos);
            tank.create();
            return true;
        }

        return false;
    }

    private boolean isAir(World world, Cuboid6i cuboid)
    {
        for (BlockPos pos : cuboid)
            if (!world.isAirBlock(pos))
                return false;
        return true;
    }
}
