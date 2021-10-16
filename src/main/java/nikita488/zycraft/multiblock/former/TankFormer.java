package nikita488.zycraft.multiblock.former;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.multiblock.TankMultiBlock;
import nikita488.zycraft.multiblock.area.AirMultiArea;
import nikita488.zycraft.multiblock.child.IMultiChildMatcher;
import nikita488.zycraft.multiblock.child.MultiChildType;
import nikita488.zycraft.util.Cuboid6i;

import javax.annotation.Nullable;

public class TankFormer
{
    public static boolean form(BlockState interfaceState, World level, BlockPos interfacePos, @Nullable Direction formingSide)
    {
        if (formingSide == null)
            return false;

        Cuboid6i innerArea = AirMultiArea.TANK.getArea(level, interfacePos, formingSide);

        if (innerArea == null)
            return false;

        FaceMatcher faceMatcher = new FaceMatcher();
        MultiPattern.Builder builder = MultiPattern.builder(innerArea.width() + 2, innerArea.height() + 2, innerArea.depth() + 2);
        MultiPattern pattern = builder
                .frame(MultiChildType.HARD)
                .faces(faceMatcher)
                .cuboid(1, 1, 1, builder.width() - 2, builder.height() - 2, builder.depth() - 2, IMultiChildMatcher.ALWAYS_MATCHES)
                .build();
        BlockPos basePos = innerArea.min().offset(-1, -1, -1);

        if (!pattern.matches(level, basePos) || !faceMatcher.hasValves())
            return false;

        TankMultiBlock tank = new TankMultiBlock(level, innerArea);

        pattern.convert(level, basePos, tank);
        tank.form();
        return true;
    }

    private static class FaceMatcher implements IMultiChildMatcher
    {
        private int valveCount;

        @Override
        public boolean matches(IBlockReader getter, BlockPos pos)
        {
            MultiChildType type = MultiChildType.get(getter, pos);

            if (type == MultiChildType.HARD || type == MultiChildType.GLASS)
                return true;

            TileEntity blockEntity = getter.getBlockEntity(pos);

            if (blockEntity == null)
                return false;

            if (ZYTiles.VALVE.is(blockEntity))
            {
                this.valveCount++;
                return true;
            }

            return ZYTiles.ITEM_IO.is(blockEntity);
        }

        private boolean hasValves()
        {
            return valveCount > 0;
        }
    }
}
