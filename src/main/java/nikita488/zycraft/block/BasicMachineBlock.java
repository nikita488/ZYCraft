package nikita488.zycraft.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import nikita488.zycraft.enums.ZYType;

import java.util.Random;

public class BasicMachineBlock extends ZYBlock
{
    private final Direction[] VALUES = Direction.values();

    public BasicMachineBlock(ZYType type, Properties properties)
    {
        super(type, properties);
    }

    @Override
    public boolean ticksRandomly(BlockState state)
    {
        return type == ZYType.GREEN;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if (type != ZYType.GREEN)
            return;

        BlockPos tickPos = pos.up();
        BlockState stateToTick = world.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.ticksRandomly())
            return;

        if (blockToTick instanceof IPlantable ||
                (blockToTick instanceof IGrowable && ((IGrowable)blockToTick).canGrow(world, tickPos, stateToTick, false)))
        {
            BlockPos.Mutable checkPos = new BlockPos.Mutable(tickPos);

            while (blockToTick == stateToTick.getBlock())
                stateToTick = world.getBlockState(checkPos.move(Direction.UP));


            stateToTick = world.getBlockState(checkPos.move(Direction.DOWN));
            stateToTick.tick(world, checkPos.toImmutable(), rand);
        }
        else if (blockToTick == this)
            stateToTick.tick(world, tickPos, rand);
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
    public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side)
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
        IFluidState fluidState = world.getFluidState(adjacentPos);

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
