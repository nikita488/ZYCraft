package nikita488.zycraft.multiblock.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.multiblock.tile.ItemIOTile;
import nikita488.zycraft.multiblock.tile.ValveTile;

import javax.annotation.Nullable;
import java.util.Optional;

public class ValveBlock extends SidedInterfaceBlock
{
    public ValveBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.VALVE.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ValveTile valve = ZYTiles.VALVE.getNullable(world, pos);

        if (valve == null)
            return ActionResultType.CONSUME;

        ItemStack heldStack = player.getHeldItem(hand);

        if (heldStack.isEmpty())
        {
            if (!player.isSneaking())
                return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
        else if (heldStack.getItem() != Items.STICK)
        {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }

        if (!world.isRemote)
            valve.toggleIO();

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        ValveTile valve = ZYTiles.VALVE.getNullable(world, pos);

        if (valve == null)
            return 0;

        Optional<IFluidHandler> capability = valve.getMultiCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, -1).resolve();

        if (!capability.isPresent())
            return 0;

        IFluidHandler handler = capability.get();
        float proportion = (float)handler.getFluidInTank(0).getAmount() / handler.getTankCapacity(0);

        return MathHelper.floor(proportion * 15);
    }
}
