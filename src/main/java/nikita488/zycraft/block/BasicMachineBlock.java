package nikita488.zycraft.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import nikita488.zycraft.enums.ZYType;

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

        ItemStack stack = player.getHeldItem(hand);

        if (stack.isEmpty())
            return ActionResultType.PASS;

        return player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(inventory ->
        {
            FluidTank tank = new FluidTank(1000);
            tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);

            FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, tank, inventory, 1000, player, true);

            if (!result.isSuccess())
                return ActionResultType.CONSUME;

            if (!world.isRemote)
                player.setHeldItem(hand, result.getResult());

            return ActionResultType.SUCCESS;
        }).orElse(ActionResultType.CONSUME);
/*        return FluidUtil.getFluidHandler(stack).map(handler ->
        {
            FluidStack water = new FluidStack(Fluids.WATER, 1000);

            if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
                return ActionResultType.CONSUME;

            if (world.isRemote)
                return ActionResultType.SUCCESS;

            handler.fill(water, IFluidHandler.FluidAction.EXECUTE);
            player.playSound(water.getFluid().getAttributes().getFillSound(water), SoundCategory.BLOCKS, 1.0F, 1.0F);

            ItemStack container = handler.getContainer();

*//*            if (container.isEmpty())
                return ActionResultType.SUCCESS;*//*

            if (container.getCount() == 1)
            {
                player.setHeldItem(hand, container);
            }
            else if (container.getCount() > 1 && player.inventory.addItemStackToInventory(container))
            {
                container.shrink(1);
            }
            else
            {
                player.dropItem(container, false, true);
                container.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }).orElse(ActionResultType.CONSUME);*/
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
