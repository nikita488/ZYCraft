package nikita488.zycraft.block;

import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.util.ParticleSpawn;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class ImmortalViewerBlock extends ColorableBlock
{
    protected final ViewerType type;

    public ImmortalViewerBlock(ViewerType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (type == ViewerType.GLOWING_IMMORTAL)
            ParticleSpawn.glowingColorableBlock(state, world, pos, rand);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type == ViewerType.DARK_IMMORTAL ? 15 : super.getOpacity(state, world, pos);
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

/*    @Override
    public boolean causesSuffocation(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
    {
        return false;
    }*/

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side)
    {
        return adjacentState.getBlock() == this;
    }
}
