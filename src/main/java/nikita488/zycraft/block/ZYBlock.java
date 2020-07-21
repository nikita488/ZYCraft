package nikita488.zycraft.block;

import nikita488.zycraft.enums.ZYType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ZYBlock extends Block
{
    protected final ZYType type;

    public ZYBlock(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

    @Override
    public MaterialColor getMaterialColor(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type.mtlColor();
    }

    public ZYType type()
    {
        return type;
    }
}
