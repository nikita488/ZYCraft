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
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.FABRICATOR.create();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos adjPos)
    {
        if (world.isClientSide())
            return;

        Direction side = Direction.getNearest(adjPos.getX() - pos.getX(), adjPos.getY() - pos.getY(), adjPos.getZ() - pos.getZ());

        if (side != Direction.UP)
            ZYTiles.FABRICATOR.get(world, pos).ifPresent(fabricator -> fabricator.logic().setSideChanged(side));
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (world.isClientSide() || state.is(newState.getBlock()))
            return;

        FabricatorTile fabricator = ZYTiles.FABRICATOR.getNullable(world, pos);

        if (fabricator != null)
        {
            fabricator.dropItems();
            world.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos)
    {
        return ZYTiles.FABRICATOR.get(world, pos).map(FabricatorTile::getComparatorInputOverride).orElse(0);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isClientSide())
            NetworkHooks.openGui((ServerPlayerEntity)player, ZYTiles.FABRICATOR.getNullable(world, pos));
        return ActionResultType.sidedSuccess(world.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(MODE);
    }
}
