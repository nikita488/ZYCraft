package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.enums.ViewerType;
import nikita488.zycraft.util.ParticleUtils;

public class ViewerBlock extends AbstractGlassBlock
{
    protected final ViewerType type;

    public ViewerBlock(ViewerType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return type == ViewerType.DARK ? getter.getMaxLightLevel() : super.getLightBlock(state, getter, pos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos)
    {
        return type != ViewerType.DARK;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (type == ViewerType.GLOWING)
            ParticleUtils.glowingBlock(state, level, pos, random);
    }

    public ViewerType type()
    {
        return type;
    }
}
