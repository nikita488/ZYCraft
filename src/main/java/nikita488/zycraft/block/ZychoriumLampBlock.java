package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.util.ParticleUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class ZychoriumLampBlock extends ColorableBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final boolean inverted;

    public ZychoriumLampBlock(boolean inverted, Properties properties)
    {
        super(properties);
        this.inverted = inverted;
        setDefaultState(getDefaultState().with(LIT, inverted));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.ZYCHORIUM_LAMP.create();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (state.get(LIT))
            ParticleUtils.glowingColorableBlock(state, world, pos, rand);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(LIT, inverted != context.getWorld().isBlockPowered(context.getPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (world.isRemote())
            return;

        boolean lit = isLit(state);

        if (lit != world.isBlockPowered(pos))
            if (lit)
                world.getPendingBlockTicks().scheduleTick(pos, this, 4);
            else
                world.setBlockState(pos, state.cycleValue(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (isLit(state) && !world.isBlockPowered(pos))
            world.setBlockState(pos, state.cycleValue(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private boolean isLit(BlockState state)
    {
        return inverted != state.get(LIT);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    public boolean inverted()
    {
        return inverted;
    }
}
