package nikita488.zycraft.container;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import nikita488.zycraft.network.UpdateContainerVarPacket;
import nikita488.zycraft.network.ZYChannel;

import javax.annotation.Nullable;

public class ZYContainer extends Container
{
    private final ObjectList<IContainerVar> containerVars = new ObjectArrayList<>();

    public ZYContainer(@Nullable ContainerType<?> type, int id)
    {
        super(type, id);
    }

    protected static void assertInventorySize(IItemHandler inventory, int minSize)
    {
        int size = inventory.getSlots();

        if (size < minSize)
            throw new IllegalArgumentException("Container size " + size + " is smaller than expected " + minSize);
    }

    protected static void assertVarListSize(ObjectList<IContainerVar> containerVars, int minSize)
    {
        int size = containerVars.size();

        if (size < minSize)
            throw new IllegalArgumentException("Container size " + size + " is smaller than expected " + minSize);
    }

    public void addPlayerInventorySlots(PlayerInventory inventory)
    {
        addPlayerInventorySlots(inventory, 8, 84);
    }

    public void addPlayerInventorySlots(PlayerInventory inventory, int x, int y)
    {
        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, x + j * 18, y + i * 18));

        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, x + i * 18, y + 58));
    }

    public void addContainerVar(IContainerVar var)
    {
        containerVars.add(var);
    }

    public void handleContainerVar(int index, PacketBuffer buffer)
    {
        containerVars.get(index).decode(buffer);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < containerVars.size(); i++)
        {
            IContainerVar var = containerVars.get(i);

            if (!var.isDirty())
                continue;

            for (IContainerListener listener : listeners)
                if (listener instanceof ServerPlayerEntity)
                    ZYChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity)listener)), new UpdateContainerVarPacket(windowId, i, var));
        }
    }

    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotIndex);
        if (slot == null || !slot.getHasStack())
            return stack;

        ItemStack slotStack = slot.getStack();
        stack = slotStack.copy();

        if (!tryTransferStackToSlot(slotStack, slotIndex))
            return ItemStack.EMPTY;

        if (slotStack.isEmpty())
            slot.putStack(ItemStack.EMPTY);
        else
            slot.onSlotChanged();

        if (slotStack.getCount() == stack.getCount())
            return ItemStack.EMPTY;

        slot.onTake(player, slotStack);

        return stack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return false;
    }
}
