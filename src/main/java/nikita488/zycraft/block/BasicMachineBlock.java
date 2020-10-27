package nikita488.zycraft.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import nikita488.zycraft.enums.ZYType;

import java.util.Optional;
import java.util.Random;

public class BasicMachineBlock extends Block
{
    private final ZYType type;
    private final Direction[] VALUES = Direction.values();

    public BasicMachineBlock(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return type == ZYType.BLUE ? Fluids.WATER.getFlowingFluidState(1, false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (type != ZYType.BLUE)
            return ActionResultType.PASS;

        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.isEmpty())
            return ActionResultType.PASS;

        Optional<IFluidHandlerItem> capability = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldStack, 1)).resolve();

        if (!capability.isPresent())
            return ActionResultType.PASS;

        IFluidHandlerItem handler = capability.get();
        FluidStack water = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
            return ActionResultType.PASS;

        player.addStat(Stats.ITEM_USED.get(heldStack.getItem()));
        player.playSound(water.getFluid().getAttributes().getFillSound(), 1.0F, 1.0F);

        if (world.isRemote)
            return ActionResultType.SUCCESS;

        handler.fill(water, IFluidHandler.FluidAction.EXECUTE);

        ItemStack filledContainer = DrinkHelper.fill(heldStack, player, handler.getContainer(), false);
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        if (heldStack != filledContainer)
            player.setHeldItem(hand, filledContainer);

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean ticksRandomly(BlockState state)
    {
        return type == ZYType.GREEN;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if (type != ZYType.GREEN)
            return;

        BlockPos tickPos = pos.up();
        BlockState stateToTick = world.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.ticksRandomly())
            return;

        if (blockToTick instanceof IPlantable || (blockToTick instanceof IGrowable && ((IGrowable)blockToTick).canGrow(world, tickPos, stateToTick, false)))
        {
            BlockPos.Mutable checkPos = new BlockPos.Mutable().setPos(tickPos);

            while (blockToTick == stateToTick.getBlock())
                stateToTick = world.getBlockState(checkPos.move(Direction.UP));

            stateToTick = world.getBlockState(checkPos.move(Direction.DOWN));
            stateToTick.randomTick(world, checkPos.toImmutable(), rand);
        }
        else if (blockToTick == this)
        {
            stateToTick.randomTick(world, tickPos, rand);
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
        return type == ZYType.GREEN && facing == Direction.UP;
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos)
    {
        return type == ZYType.GREEN;
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side)
    {
        return type == ZYType.RED && side == Direction.UP;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (type == ZYType.GREEN || type == ZYType.RED)
            return;

        for (Direction side : VALUES)
            modifyAdjacentState(world, pos, pos.offset(side));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjacentPos, boolean isMoving)
    {
        if (type == ZYType.GREEN || type == ZYType.RED)
            return;

        modifyAdjacentState(world, pos, adjacentPos);
    }

    private void modifyAdjacentState(World world, BlockPos pos, BlockPos adjacentPos)
    {
        BlockState state = world.getBlockState(adjacentPos);
        Block block = state.getBlock();
        FluidState fluidState = world.getFluidState(adjacentPos);

        if (fluidState.isEmpty())
            return;

        if (type == ZYType.BLUE)
        {
            if (!fluidState.isTagged(FluidTags.LAVA))
                return;

            if (fluidState.isSource())
                state = Blocks.OBSIDIAN.getDefaultState();
            else if (fluidState.getActualHeight(world, adjacentPos) >= 0.44444445F)
                state = Blocks.COBBLESTONE.getDefaultState();
            else
                return;
            world.setBlockState(adjacentPos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, adjacentPos, pos, state));
            world.playEvent(Constants.WorldEvents.LAVA_EXTINGUISH, adjacentPos, -1);
        }
        else if (type == ZYType.DARK)
        {
            Material material = state.getMaterial();

            if (block instanceof IBucketPickupHandler && ((IBucketPickupHandler)block).pickupFluid(world, adjacentPos, state) != Fluids.EMPTY)
            {
                return;
            }
            else if (block instanceof FlowingFluidBlock)
            {
                world.setBlockState(adjacentPos, Blocks.AIR.getDefaultState());
            }
            else if (material == Material.OCEAN_PLANT || material == Material.SEA_GRASS)
            {
                spawnDrops(state, world, adjacentPos, state.hasTileEntity() ? world.getTileEntity(adjacentPos) : null);
                world.setBlockState(adjacentPos, Blocks.AIR.getDefaultState());
            }
        }
        else if (type == ZYType.LIGHT)
        {
            if (!fluidState.isTagged(FluidTags.WATER) || fluidState.getActualHeight(world, adjacentPos) < 0.44444445F || !(block instanceof FlowingFluidBlock))
                return;
            state = Blocks.ICE.getDefaultState();
            world.setBlockState(adjacentPos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, adjacentPos, pos, state));
        }
    }
}
