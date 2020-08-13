package nikita488.zycraft.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import nikita488.zycraft.api.multiblock.MultiType;
import nikita488.zycraft.api.multiblock.child.MultiChildBlock;
import nikita488.zycraft.api.multiblock.child.MultiChildTile;

public abstract class MultiInterfaceBlock extends MultiChildBlock
{
    public MultiInterfaceBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            MultiChildTile child = getChildTile(world, pos);

            if (child == null)
                return ActionResultType.CONSUME;

            if (!child.hasParent() && !player.isSneaking())
                return MultiType.formAny(world, pos);

            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }

        return ActionResultType.SUCCESS;
    }
}
