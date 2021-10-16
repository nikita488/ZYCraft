package nikita488.zycraft.multiblock.area;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class AirMultiArea implements IMultiArea
{
    public static final AirMultiArea TANK = new AirMultiArea(10);
    private final int maxSize;

    public AirMultiArea(int maxSize)
    {
        this.maxSize = maxSize;
    }

    @Override
    public int maxSize(Direction side)
    {
        return maxSize;
    }

    @Override
    public boolean matches(Level level, BlockPos pos)
    {
        return level.isEmptyBlock(pos);
    }
}
