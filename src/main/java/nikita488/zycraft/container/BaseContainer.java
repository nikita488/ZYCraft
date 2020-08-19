package nikita488.zycraft.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import nikita488.zycraft.network.ZYChannel;
import nikita488.zycraft.network.UpdateContainerVarPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainer extends Container
{
    private final List<IContainerVar> containerVars = new ArrayList<>();

    public BaseContainer(@Nullable ContainerType<?> type, int windowID)
    {
        super(type, windowID);
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

    public void handleUpdateVar(int index, PacketBuffer buffer)
    {
        containerVars.get(index).decode(buffer);
    }

    public boolean tryTransferStackInSlot(ItemStack stack, int slotIndex)
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

        if (!tryTransferStackInSlot(slotStack, slotIndex))
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
    public ItemStack slotClick(int index, int dragType, ClickType type, PlayerEntity player)
    {
        if (index < 0 || index >= inventorySlots.size())
            return ItemStack.EMPTY;

        Slot slot = getSlot(index);
        if (slot instanceof IClickableSlot)
            return ((IClickableSlot)slot).onClick(this, index, dragType, type, player);

        return super.slotClick(index, dragType, type, player);
    }
}
