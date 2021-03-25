package nikita488.zycraft.multiblock.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.MultiManager;
import nikita488.zycraft.multiblock.MultiType;
import nikita488.zycraft.multiblock.tile.MultiChildTile;

public abstract class MultiInterfaceBlock extends MultiChildBlock
{
    public MultiInterfaceBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        MultiChildTile child = getChild(world, pos);

        if (child == null)
            return ActionResultType.CONSUME;

        if (child.hasParent())
            return super.onBlockActivated(state, world, pos, player, hand, hit);

        if (!world.isRemote && !player.isSneaking() && MultiManager.tryFormMultiBlock(world, pos, hit.getFace()))
            return ActionResultType.SUCCESS;

        return ActionResultType.CONSUME;
    }
}
