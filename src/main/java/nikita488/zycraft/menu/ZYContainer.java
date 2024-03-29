package nikita488.zycraft.menu;

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
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.menu.data.IMenuData;
import nikita488.zycraft.network.UpdateMenuDataPacket;

import javax.annotation.Nullable;

public abstract class ZYContainer extends Container
{
    public static final Container EMPTY_CONTAINER = new Container(null, -1)
    {
        @Override
        public boolean stillValid(PlayerEntity player)
        {
            return false;
        }
    };
    protected final ObjectList<IMenuData> data = new ObjectArrayList<>();

    public ZYContainer(@Nullable ContainerType<?> type, int id)
    {
        super(type, id);
    }

    protected static void assertInventorySize(IItemHandler inventory, int minSize)
    {
        int size = inventory.getSlots();

        if (size < minSize)
            throw new IllegalArgumentException("Inventory size " + size + " is smaller than expected " + minSize);
    }

    protected static void assertDataSize(ObjectList<IMenuData> data, int minSize)
    {
        int size = data.size();

        if (size < minSize)
            throw new IllegalArgumentException("Data size " + size + " is smaller than expected " + minSize);
    }

    public void addPlayerInventorySlots(PlayerInventory inventory)
    {
        addPlayerInventorySlots(inventory, 16, 92);
    }

    public void addPlayerInventorySlots(PlayerInventory inventory, int x, int y)
    {
        for(int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(inventory, j + i * 9 + 9, x + j * 18, y + i * 18));

        for(int i = 0; i < 9; i++)
            addSlot(new Slot(inventory, i, x + i * 18, y + 58));
    }

    public void addVariable(IMenuData variable)
    {
        data.add(variable);
    }

    public void handleVariable(int index, PacketBuffer buffer)
    {
        data.get(index).decode(buffer);
    }

    @Override
    public void broadcastChanges()
    {
        super.broadcastChanges();

        if (containerListeners.isEmpty())
            return;

        for (int i = 0; i < data.size(); i++)
        {
            IMenuData variable = data.get(i);

            if (!variable.canBeUpdated())
                continue;

            for (IContainerListener listener : containerListeners)
                if (listener instanceof ServerPlayerEntity)
                    ZYCraft.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity)listener)), new UpdateMenuDataPacket(containerId, i, variable));
        }
    }

    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem())
            return stack;

        ItemStack slotStack = slot.getItem();
        stack = slotStack.copy();

        if (!tryTransferStackToSlot(slotStack, slotIndex))
            return ItemStack.EMPTY;

        if (slotStack.isEmpty())
            slot.set(ItemStack.EMPTY);
        else
            slot.setChanged();

        if (slotStack.getCount() == stack.getCount())
            return ItemStack.EMPTY;

        slot.onTake(player, slotStack);

        return stack;
    }
}
