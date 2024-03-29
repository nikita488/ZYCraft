package nikita488.zycraft.util;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtils
{
    public static void dropInventoryItems(World level, BlockPos pos, IItemHandler inventory)
    {
        dropInventoryItems(level, pos.getX(), pos.getY(), pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World level, double x, double y, double z, IItemHandler inventory)
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
            InventoryHelper.dropItemStack(level, x, y, z, inventory.getStackInSlot(slot));
    }
}
