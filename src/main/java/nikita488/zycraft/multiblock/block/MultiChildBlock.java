package nikita488.zycraft.multiblock.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nikita488.zycraft.multiblock.MultiBlock;
import nikita488.zycraft.multiblock.tile.MultiChildTile;

import javax.annotation.Nullable;

public abstract class MultiChildBlock extends Block
{
    public MultiChildBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    protected MultiChildTile getChild(IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof MultiChildTile ? (MultiChildTile)tile : null;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        int lightValue = state.getLightValue();
        MultiChildTile child = getChild(world, pos);

        if (child == null)
            return lightValue;

        for (MultiBlock multiBlock : child.parentMultiBlocks())
            lightValue = Math.max(lightValue, multiBlock.getChildLightValue(child));

        return lightValue;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        MultiChildTile child = getChild(world, pos);

        if (child == null)
            return ActionResultType.CONSUME;

        for (MultiBlock multiBlock : child.parentMultiBlocks())
        {
            ActionResultType action = multiBlock.onChildInteraction(child, player, hand, hit);

            if (action.isSuccessOrConsume())
                return action;
        }

        return ActionResultType.PASS;
    }
}
