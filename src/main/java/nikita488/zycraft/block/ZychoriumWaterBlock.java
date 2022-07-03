package nikita488.zycraft.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.Optional;

public class ZychoriumWaterBlock extends Block implements IFluidSource
{
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ZychoriumWaterBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return !state.getValue(POWERED) ? Fluids.WATER.getFlowing(1, false) : Fluids.EMPTY.defaultFluidState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (level.hasNeighborSignal(pos))
            return;

        for (Direction side : ZYConstants.DIRECTIONS)
        {
            BlockPos relativePos = pos.relative(side);
            FluidState fluidState = level.getFluidState(relativePos);

            if (!fluidState.is(FluidTags.LAVA))
                continue;

            level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, (fluidState.isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE).defaultBlockState()));
            level.levelEvent(LevelEvent.LAVA_FIZZ, relativePos, -1);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        if (level.hasNeighborSignal(pos) != state.getValue(POWERED))
            level.setBlock(pos, state.cycle(POWERED), UPDATE_CLIENTS);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        ItemStack heldStack = player.getItemInHand(hand);
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return InteractionResult.PASS;

        IFluidHandlerItem handler = capability.get();
        FluidStack water = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);

        if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
            return InteractionResult.PASS;

        player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
        Optional.ofNullable(water.getFluid().getFluidType().getSound(player, level, pos, SoundActions.BUCKET_FILL))
                .ifPresent(sound -> player.playSound(sound, 1F, 1F));
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        handler.fill(water, IFluidHandler.FluidAction.EXECUTE);

        ItemStack filledContainer = ItemUtils.createFilledResult(heldStack, player, handler.getContainer(), false);
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, filledContainer);

        if (heldStack != filledContainer)
            player.setItemInHand(hand, filledContainer);

        return InteractionResult.CONSUME;
    }

    @Override
    public FluidStack getFluid(BlockState state, Level level, BlockPos pos, @Nullable Direction side)
    {
        return !state.getValue(POWERED) ? new FluidStack(Fluids.WATER, 50) : FluidStack.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED);
    }
}
