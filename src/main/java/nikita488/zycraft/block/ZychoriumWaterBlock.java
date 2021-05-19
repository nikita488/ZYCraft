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
import nikita488.zycraft.util.FluidUtils;

import java.util.Optional;

public class ZychoriumWaterBlock extends Block
{
    private static final Direction[] VALUES = Direction.values();

    public ZychoriumWaterBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return Fluids.WATER.getFlowingFluidState(1, false);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : VALUES)
            transform(world, pos, pos.offset(side));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjPos, boolean isMoving)
    {
        transform(world, pos, adjPos);
    }

    private void transform(World world, BlockPos pos, BlockPos adjPos)
    {
        FluidState fluidState = world.getFluidState(adjPos);

        if (!fluidState.isTagged(FluidTags.LAVA))
            return;

        world.setBlockState(adjPos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, adjPos, pos, (fluidState.isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE).getDefaultState()));
        world.playEvent(Constants.WorldEvents.LAVA_EXTINGUISH, adjPos, -1);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.isEmpty())
            return ActionResultType.PASS;

        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return ActionResultType.PASS;

        IFluidHandlerItem handler = capability.get();
        FluidStack water = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
            return ActionResultType.PASS;

        player.addStat(Stats.ITEM_USED.get(heldStack.getItem()));
        player.playSound(water.getFluid().getAttributes().getFillSound(), 1, 1);

        if (world.isRemote())
            return ActionResultType.SUCCESS;

        handler.fill(water, IFluidHandler.FluidAction.EXECUTE);

        ItemStack filledContainer = DrinkHelper.fill(heldStack, player, handler.getContainer(), false);
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        if (heldStack != filledContainer)
            player.setHeldItem(hand, filledContainer);

        return ActionResultType.SUCCESS;
    }
}
