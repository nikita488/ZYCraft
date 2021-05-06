package nikita488.zycraft.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtils
{
    public static CompoundNBT write(IInventory inventory)
    {
        CompoundNBT tag = new CompoundNBT();
        ListNBT itemTags = new ListNBT();

        for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);

            if (stack.isEmpty())
                continue;

            CompoundNBT itemTag = new CompoundNBT();

            itemTag.putInt("Slot", slot);
            stack.write(itemTag);
            itemTags.add(itemTag);
        }

        tag.put("Items", itemTags);

        return tag;
    }

    public static void read(IInventory inventory, CompoundNBT tag)
    {
        ListNBT itemTags = tag.getList("Items", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < itemTags.size(); i++)
        {
            CompoundNBT itemTag = itemTags.getCompound(i);
            int slot = itemTag.getInt("Slot");

            if (slot >= 0 && slot < inventory.getSizeInventory())
                inventory.setInventorySlotContents(slot, ItemStack.read(itemTag));
        }
    }

    public static void dropInventoryItems(World world, BlockPos pos, IItemHandler inventory)
    {
        dropInventoryItems(world, pos.getX(), pos.getY(), pos.getZ(), inventory);
    }

    public static void dropInventoryItems(World world, double x, double y, double z, IItemHandler inventory)
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
            InventoryHelper.spawnItemStack(world, x, y, z, inventory.getStackInSlot(slot));
    }
}
