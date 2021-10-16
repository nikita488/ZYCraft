package nikita488.zycraft.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.util.FluidUtils;
import nikita488.zycraft.util.ZYConstants;

import javax.annotation.Nullable;
import java.util.Optional;

public class ZychoriumWaterBlock extends Block implements IFluidSource
{
    public ZychoriumWaterBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return Fluids.WATER.getFlowing(1, false);
    }

    @Override
    public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : ZYConstants.DIRECTIONS)
            transform(level, pos, pos.relative(side));
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos relativePos, boolean isMoving)
    {
        transform(level, pos, relativePos);
    }

    private void transform(World level, BlockPos pos, BlockPos relativePos)
    {
        FluidState fluidState = level.getFluidState(relativePos);

        if (!fluidState.is(FluidTags.LAVA))
            return;

        level.setBlockAndUpdate(relativePos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, relativePos, pos, (fluidState.isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE).defaultBlockState()));
        level.levelEvent(Constants.WorldEvents.LAVA_EXTINGUISH, relativePos, -1);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        ItemStack heldStack = player.getItemInHand(hand);
        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return ActionResultType.PASS;

        IFluidHandlerItem handler = capability.get();
        FluidStack water = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
            return ActionResultType.PASS;

        player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
        player.playSound(water.getFluid().getAttributes().getFillSound(), 1F, 1F);

        if (level.isClientSide())
            return ActionResultType.SUCCESS;

        handler.fill(water, IFluidHandler.FluidAction.EXECUTE);

        ItemStack filledContainer = DrinkHelper.createFilledResult(heldStack, player, handler.getContainer(), false);
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        if (heldStack != filledContainer)
            player.setItemInHand(hand, filledContainer);

        return ActionResultType.CONSUME;
    }

    @Override
    public FluidStack getFluid(BlockState state, World level, BlockPos pos, @Nullable Direction side)
    {
        return new FluidStack(Fluids.WATER, 50);
    }
}
