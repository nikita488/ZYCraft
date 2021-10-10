package nikita488.zycraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.tile.FluidSelectorTile;

import javax.annotation.Nullable;
import java.util.List;

public class FluidSelectorBlock extends Block implements IFluidSource
{
    public FluidSelectorBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.FLUID_SELECTOR.create();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(ZYLang.CREATIVE_ONLY);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        FluidStack containedFluid = FluidUtil.getFluidContained(player.getHeldItem(hand)).orElse(FluidStack.EMPTY);

        if (containedFluid.isEmpty())
            return ActionResultType.PASS;

        FluidSelectorTile selector = ZYTiles.FLUID_SELECTOR.getNullable(world, pos);

        if (selector != null)
            if (selector.getSelectedFluid().isFluidEqual(containedFluid))
                return ActionResultType.CONSUME;
            else if (!world.isRemote())
                selector.setSelectedFluid(containedFluid);

        return ActionResultType.func_233537_a_(world.isRemote());
    }

    @Override
    public FluidStack getFluid(BlockState state, World world, BlockPos pos, @Nullable Direction side)
    {
        FluidStack fluid = ZYTiles.FLUID_SELECTOR.get(world, pos).map(FluidSelectorTile::getSelectedFluid).orElse(FluidStack.EMPTY);

        if (fluid.isEmpty())
            return FluidStack.EMPTY;

        fluid = fluid.copy();
        fluid.setAmount(150 - world.getRedstonePowerFromNeighbors(pos) * 10);
        return fluid;
    }
}