package nikita488.zycraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtils
{
    public static void dropInventoryItems(Level level, BlockPos pos, IItemHandler inventory)
    {
        dropInventoryItems(level, pos.getX(), pos.getY(), pos.getZ(), inventory);
    }

    public static void dropInventoryItems(Level level, double x, double y, double z, IItemHandler inventory)
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
            Containers.dropItemStack(level, x, y, z, inventory.getStackInSlot(slot));
    }
}
