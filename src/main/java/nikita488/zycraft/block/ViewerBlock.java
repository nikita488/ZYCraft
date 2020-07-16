package nikita488.zycraft.block;

import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.util.ParticleSpawn;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class ViewerBlock extends AbstractGlassBlock
{
    protected final ViewerType type;

    public ViewerBlock(ViewerType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type == ViewerType.DARK ? 15 : super.getOpacity(state, world, pos);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (type == ViewerType.GLOWING)
            ParticleSpawn.glowingBlock(state, world, pos, rand);
    }

    public ViewerType type()
    {
        return type;
    }
}
