package nikita488.zycraft.block;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.util.ParticleUtils;

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
    public int getLightBlock(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type == ViewerType.DARK ? world.getMaxLightLevel() : super.getLightBlock(state, world, pos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type != ViewerType.DARK;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (type == ViewerType.GLOWING)
            ParticleUtils.glowingBlock(state, world, pos, rand);
    }

    public ViewerType type()
    {
        return type;
    }
}
