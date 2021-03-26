package nikita488.zycraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.util.ParticleSpawn;

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
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (type == ViewerType.GLOWING)
            ParticleSpawn.glowingColorableBlock(state, world, pos, rand);
    }

    @Override
    public int getLightBlock(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type == ViewerType.DARK ? 15 : super.getLightBlock(state, world, pos);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.empty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader world, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side)
    {
        return adjacentState.is(this);
    }
}
