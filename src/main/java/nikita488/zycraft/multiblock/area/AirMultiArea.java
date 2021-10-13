package nikita488.zycraft.multiblock.area;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public boolean matches(World world, BlockPos pos)
    {
        return world.isEmptyBlock(pos);
    }
}
