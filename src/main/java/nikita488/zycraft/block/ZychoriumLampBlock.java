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
        registerDefaultState(defaultBlockState().setValue(LIT, inverted));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.ZYCHORIUM_LAMP.create();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World level, BlockPos pos, Random random)
    {
        if (state.getValue(LIT))
            ParticleUtils.glowingColorableBlock(state, level, pos, random);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState().setValue(LIT, inverted != context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (level.isClientSide())
            return;

        boolean lit = isLit(state);

        if (lit != level.hasNeighborSignal(pos))
            if (lit)
                level.getBlockTicks().scheduleTick(pos, this, 4);
            else
                level.setBlock(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (isLit(state) && !world.hasNeighborSignal(pos))
            world.setBlock(pos, state.cycle(LIT), Constants.BlockFlags.BLOCK_UPDATE);
    }

    private boolean isLit(BlockState state)
    {
        return inverted != state.getValue(LIT);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    public boolean inverted()
    {
        return inverted;
    }
}
