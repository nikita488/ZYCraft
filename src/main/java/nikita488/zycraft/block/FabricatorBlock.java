package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.tile.FabricatorTile;
import nikita488.zycraft.util.BlockEntityUtils;

import javax.annotation.Nullable;

public class FabricatorBlock extends Block implements EntityBlock
{
    public static final EnumProperty<FabricatorMode> MODE = ZYBlockStateProperties.FABRICATOR_MODE;

    public FabricatorBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(MODE, FabricatorMode.AUTO_LOW));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ZYTiles.FABRICATOR.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return level.isClientSide() ? null : BlockEntityUtils.createTickerHelper(type, ZYTiles.FABRICATOR.get(), FabricatorTile::serverTick);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader reader, BlockPos pos, BlockPos relativePos)
    {
        if (reader.isClientSide())
            return;

        Direction side = Direction.getNearest(relativePos.getX() - pos.getX(), relativePos.getY() - pos.getY(), relativePos.getZ() - pos.getZ());

        if (side != Direction.UP)
            ZYTiles.FABRICATOR.get(reader, pos).ifPresent(fabricator -> fabricator.logic().setSideChanged(side));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (level.isClientSide() || state.is(newState.getBlock()))
            return;

        FabricatorTile fabricator = ZYTiles.FABRICATOR.getNullable(level, pos);

        if (fabricator != null)
        {
            fabricator.dropItems();
            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return ZYTiles.FABRICATOR.get(level, pos).map(FabricatorTile::getAnalogOutputSignal).orElse(0);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (!level.isClientSide())
            NetworkHooks.openGui((ServerPlayer)player, ZYTiles.FABRICATOR.getNullable(level, pos));
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(MODE);
    }
}
