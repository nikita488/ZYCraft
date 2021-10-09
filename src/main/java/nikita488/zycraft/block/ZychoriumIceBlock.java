package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import nikita488.zycraft.util.ZYConstants;

public class ZychoriumIceBlock extends Block
{
    public ZychoriumIceBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        for (Direction side : ZYConstants.DIRECTIONS)
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

        if (fluidState.isTagged(FluidTags.WATER))
        {
            if (world.getBlockState(adjPos).getBlock() instanceof FlowingFluidBlock)
                world.setBlockState(adjPos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, adjPos, pos, Blocks.ICE.getDefaultState()));
        }
        else if (fluidState.isTagged(FluidTags.LAVA) && world.getBlockState(pos.down()).matchesBlock(Blocks.SOUL_SOIL))
        {
            world.setBlockState(adjPos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, adjPos, pos, Blocks.BASALT.getDefaultState()));
            world.playEvent(Constants.WorldEvents.LAVA_EXTINGUISH, adjPos, -1);
        }
    }
}
