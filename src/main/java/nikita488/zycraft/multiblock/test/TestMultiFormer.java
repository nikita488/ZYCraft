package nikita488.zycraft.multiblock.test;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import nikita488.zycraft.api.multiblock.child.IMultiChildMatcher;
import nikita488.zycraft.api.multiblock.IMultiFormer;
import nikita488.zycraft.api.multiblock.MultiPattern;
import nikita488.zycraft.api.multiblock.MultiValidationType;
import nikita488.zycraft.api.multiblock.child.MultiChildMaterial;
import nikita488.zycraft.api.multiblock.child.MultiChildMatcher;

public class TestMultiFormer implements IMultiFormer
{
    private final Direction[] VALUES = Direction.values();
    private final IMultiChildMatcher hardAndGlass = MultiChildMatcher.of(MultiChildMaterial.HARD, MultiChildMaterial.GLASS);
    private final IMultiChildMatcher frame = MultiChildMatcher.of(MultiChildMaterial.HARD);
    private final IMultiChildMatcher face = (world, pos) ->
            hardAndGlass.matches(world, pos) || world.getTileEntity(pos) instanceof TestTile;

    @Override
    public boolean form(IWorld world, BlockPos interfacePos)
    {
        for (Direction side : VALUES)
        {
            BlockPos adjacentPos = interfacePos.offset(side);

            if (!world.isAirBlock(adjacentPos)) continue;

            BlockPos cornerPos = adjacentPos.add(-1, -1, -1);
            MultiPattern pattern = new MultiPattern(3, 3, 3);
            pattern.setFaceMatcher(face);
            pattern.setEdgeMatcher(frame);
            pattern.setMatcher(1, 1, 1, 1, 1, 1, null);

            if (!pattern.matches(world, cornerPos))
                continue;

            TestMultiBlock test = new TestMultiBlock(world, new ChunkPos(interfacePos), adjacentPos);
            pattern.addBlocks(test, cornerPos);
            test.world().addMultiBlock(test, MultiValidationType.CREATE);
            return true;
        }

        return false;
    }
}
