package nikita488.zycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import nikita488.zycraft.api.fluid.IFluidSource;
import nikita488.zycraft.init.ZYLang;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.tile.FluidSelectorTile;

import javax.annotation.Nullable;
import java.util.List;

public class FluidSelectorBlock extends Block implements EntityBlock, IFluidSource
{
    public FluidSelectorBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return ZYTiles.FLUID_SELECTOR.create(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag)
    {
        tooltip.add(ZYLang.CREATIVE_ONLY);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        FluidStack containedFluid = FluidUtil.getFluidContained(player.getItemInHand(hand)).orElse(FluidStack.EMPTY);

        if (containedFluid.isEmpty())
            return InteractionResult.PASS;

        FluidSelectorTile selector = ZYTiles.FLUID_SELECTOR.getNullable(level, pos);

        if (selector != null)
            if (selector.getSelectedFluid().isFluidEqual(containedFluid))
                return InteractionResult.CONSUME;
            else if (!level.isClientSide())
                selector.setSelectedFluid(containedFluid);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public FluidStack getFluid(BlockState state, Level level, BlockPos pos, @Nullable Direction side)
    {
        FluidStack fluid = ZYTiles.FLUID_SELECTOR.get(level, pos).map(FluidSelectorTile::getSelectedFluid).orElse(FluidStack.EMPTY);

        if (fluid.isEmpty())
            return FluidStack.EMPTY;

        fluid = fluid.copy();
        fluid.setAmount(150 - level.getBestNeighborSignal(pos) * 10);
        return fluid;
    }
}
