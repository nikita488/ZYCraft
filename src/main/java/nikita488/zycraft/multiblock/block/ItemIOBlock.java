package nikita488.zycraft.multiblock.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import nikita488.zycraft.init.ZYTiles;
import nikita488.zycraft.multiblock.tile.ItemIOTile;
import nikita488.zycraft.multiblock.tile.ValveTile;

import javax.annotation.Nullable;
import java.util.Optional;

public class ItemIOBlock extends SidedInterfaceBlock
{
    public ItemIOBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.ITEM_IO.create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (super.onBlockActivated(state, world, pos, player, hand, hit).isSuccess())
            return ActionResultType.SUCCESS;

        Optional<ItemIOTile> itemIO = ZYTiles.ITEM_IO.get(world, pos);

        if (!itemIO.isPresent())
            return ActionResultType.CONSUME;

        itemIO.get().toggleIO();
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
        ItemIOTile itemIO = ZYTiles.ITEM_IO.getNullable(world, pos);

        if (itemIO == null)
            return 0;

        Optional<IItemHandler> capability = itemIO.getMultiCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, -1).resolve();
        return capability.map(ItemHandlerHelper::calcRedstoneFromInventory).orElse(0);

    }
}
