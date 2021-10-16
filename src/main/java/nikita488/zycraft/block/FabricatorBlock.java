package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nikita488.zycraft.block.state.properties.FabricatorMode;
import nikita488.zycraft.block.state.properties.ZYBlockStateProperties;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.tile.FabricatorTile;

import javax.annotation.Nullable;

public class FabricatorBlock extends Block
{
    public static final EnumProperty<FabricatorMode> MODE = ZYBlockStateProperties.FABRICATOR_MODE;

    public FabricatorBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(MODE, FabricatorMode.AUTO_LOW));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.FABRICATOR.create();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader reader, BlockPos pos, BlockPos relativePos)
    {
        if (reader.isClientSide())
            return;

        Direction side = Direction.getNearest(relativePos.getX() - pos.getX(), relativePos.getY() - pos.getY(), relativePos.getZ() - pos.getZ());

        if (side != Direction.UP)
            ZYTiles.FABRICATOR.get(reader, pos).ifPresent(fabricator -> fabricator.logic().setSideChanged(side));
    }

    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving)
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
    public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos)
    {
        return ZYTiles.FABRICATOR.get(level, pos).map(FabricatorTile::getAnalogOutputSignal).orElse(0);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        if (!level.isClientSide())
            NetworkHooks.openGui((ServerPlayerEntity)player, ZYTiles.FABRICATOR.getNullable(level, pos));
        return ActionResultType.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(MODE);
    }
}
