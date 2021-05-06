package nikita488.zycraft.block;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class FireBasinBlock extends Block
{
    public FireBasinBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side)
    {
        return side == Direction.UP;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos adjacentPos, boolean isMoving)
    {
        BlockPos abovePos = pos.up();

        if (world.isBlockPowered(pos))
        {
            if (!AbstractFireBlock.canLightBlock(world, abovePos, Direction.UP))
                return;

            world.playSound(null, abovePos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(abovePos, AbstractFireBlock.getFireForPlacement(world, abovePos), Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
        else if (world.getBlockState(abovePos).getBlock() instanceof AbstractFireBlock)
        {
            world.setBlockState(abovePos, Blocks.AIR.getDefaultState());
        }
    }
}
