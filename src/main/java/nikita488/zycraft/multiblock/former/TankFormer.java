package nikita488.zycraft.multiblock.former;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.init.ZYBlockEntities;
import nikita488.zycraft.multiblock.TankMultiBlock;
import nikita488.zycraft.multiblock.area.AirMultiArea;
import nikita488.zycraft.multiblock.child.IMultiChildMatcher;
import nikita488.zycraft.multiblock.child.MultiChildType;
import nikita488.zycraft.util.Cuboid6i;

import javax.annotation.Nullable;

public class TankFormer
{
    public static boolean form(BlockState interfaceState, Level level, BlockPos interfacePos, @Nullable Direction formingSide)
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
        public boolean matches(BlockGetter getter, BlockPos pos)
        {
            MultiChildType type = MultiChildType.get(getter, pos);

            if (type == MultiChildType.HARD || type == MultiChildType.GLASS)
                return true;

            BlockEntity blockEntity = getter.getBlockEntity(pos);

            if (blockEntity == null)
                return false;

            if (ZYBlockEntities.VALVE.is(blockEntity))
            {
                this.valveCount++;
                return true;
            }

            return ZYBlockEntities.ITEM_IO.is(blockEntity);
        }

        private boolean hasValves()
        {
            return valveCount > 0;
        }
    }
}
