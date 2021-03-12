package nikita488.zycraft.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.FluidUtils;

import java.util.Optional;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

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
        return type == ZYType.BLUE ? Fluids.WATER.getFlowing(1, false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (type != ZYType.BLUE)
            return ActionResultType.PASS;

        ItemStack heldStack = player.getItemInHand(hand);

        if (heldStack.isEmpty())
            return ActionResultType.PASS;

        Optional<IFluidHandlerItem> capability = FluidUtils.getItemFluidHandler(heldStack);

        if (!capability.isPresent())
            return ActionResultType.PASS;

        IFluidHandlerItem handler = capability.get();
        FluidStack water = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);

        if (handler.fill(water, IFluidHandler.FluidAction.SIMULATE) <= 0)
            return ActionResultType.PASS;

        player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
        player.playSound(water.getFluid().getAttributes().getFillSound(), 1.0F, 1.0F);

        if (world.isClientSide)
            return ActionResultType.SUCCESS;

        handler.fill(water, IFluidHandler.FluidAction.EXECUTE);

        ItemStack filledContainer = DrinkHelper.createFilledResult(heldStack, player, handler.getContainer(), false);
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, filledContainer);

        if (heldStack != filledContainer)
            player.setItemInHand(hand, filledContainer);

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state)
    {
        return type == ZYType.GREEN;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if (type != ZYType.GREEN)
            return;

        BlockPos tickPos = pos.above();
        BlockState stateToTick = world.getBlockState(tickPos);
        Block blockToTick = stateToTick.getBlock();

        if (!stateToTick.isRandomlyTicking())
            return;

        if (blockToTick instanceof IPlantable || (blockToTick instanceof IGrowable && ((IGrowable)blockToTick).isValidBonemealTarget(world, tickPos, stateToTick, false)))
        {
            BlockPos.Mutable checkPos = new BlockPos.Mutable().set(tickPos);

            while (blockToTick == stateToTick.getBlock() && stateToTick.isRandomlyTicking())
                stateToTick = world.getBlockState(checkPos.move(Direction.UP));

            stateToTick = world.getBlockState(checkPos.move(Direction.DOWN));
            stateToTick.randomTick(world, checkPos.immutable(), rand);
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
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (type == ZYType.GREEN || type == ZYType.RED)
            return;

        for (Direction side : VALUES)
            modifyAdjacentState(world, pos.relative(side));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjacentPos, boolean isMoving)
    {
        if (type == ZYType.GREEN || type == ZYType.RED)
            return;

        modifyAdjacentState(world, adjacentPos);
    }

    private void modifyAdjacentState(World world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);

        if (fluidState.isEmpty())
            return;

        if (type == ZYType.BLUE)
            FluidUtils.turnLavaIntoBlock(world, pos, fluidState);
        else if (type == ZYType.DARK)
            FluidUtils.voidFluid(blockState, world, pos);
        else if (type == ZYType.LIGHT && !FluidUtils.turnLavaIntoBasalt(world, pos, fluidState))
            FluidUtils.turnWaterIntoIce(blockState, world, pos, fluidState);
    }
}
