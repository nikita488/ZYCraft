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
    public TileEntity createTileEntity(BlockState state, IBlockReader getter)
    {
        return ZYTiles.FLUID_SELECTOR.create();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader getter, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        tooltip.add(ZYLang.CREATIVE_ONLY);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hitResult)
    {
        FluidStack containedFluid = FluidUtil.getFluidContained(player.getItemInHand(hand)).orElse(FluidStack.EMPTY);

        if (containedFluid.isEmpty())
            return ActionResultType.PASS;

        FluidSelectorTile selector = ZYTiles.FLUID_SELECTOR.getNullable(level, pos);

        if (selector != null)
            if (selector.getSelectedFluid().isFluidEqual(containedFluid))
                return ActionResultType.CONSUME;
            else if (!level.isClientSide())
                selector.setSelectedFluid(containedFluid);

        return ActionResultType.sidedSuccess(level.isClientSide());
    }

    @Override
    public FluidStack getFluid(BlockState state, World level, BlockPos pos, @Nullable Direction side)
    {
        FluidStack fluid = ZYTiles.FLUID_SELECTOR.get(level, pos).map(FluidSelectorTile::getSelectedFluid).orElse(FluidStack.EMPTY);

        if (fluid.isEmpty())
            return FluidStack.EMPTY;

        fluid = fluid.copy();
        fluid.setAmount(150 - level.getBestNeighborSignal(pos) * 10);
        return fluid;
    }
}
