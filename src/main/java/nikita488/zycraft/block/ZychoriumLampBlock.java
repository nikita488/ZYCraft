package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import nikita488.zycraft.init.ZYBlockEntities;
import nikita488.zycraft.util.ParticleUtils;

import javax.annotation.Nullable;

public class ZychoriumLampBlock extends ColorableBlock implements EntityBlock
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ZYBlockEntities.ZYCHORIUM_LAMP.create(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (state.getValue(LIT))
            ParticleUtils.glowingColorableBlock(state, level, pos, random);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(LIT, inverted != context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (level.isClientSide())
            return;

        boolean lit = isLit(state);

        if (lit != level.hasNeighborSignal(pos))
            if (lit)
                level.scheduleTick(pos, this, 4);
            else
                level.setBlock(pos, state.cycle(LIT), UPDATE_CLIENTS);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (isLit(state) && !level.hasNeighborSignal(pos))
            level.setBlock(pos, state.cycle(LIT), UPDATE_CLIENTS);
    }

    private boolean isLit(BlockState state)
    {
        return inverted != state.getValue(LIT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }

    public boolean inverted()
    {
        return inverted;
    }
}
