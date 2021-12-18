package nikita488.zycraft.menu;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.menu.data.IMenuData;
import nikita488.zycraft.network.UpdateMenuDataPacket;

import javax.annotation.Nullable;

public abstract class ZYMenu extends AbstractContainerMenu
{
    public static final AbstractContainerMenu EMPTY_MENU = new AbstractContainerMenu(null, -1)
    {
        @Override
        public boolean stillValid(Player player)
        {
            return false;
        }
    };
    private final Player player;
    protected final ObjectList<IMenuData> data = new ObjectArrayList<>();

    public ZYMenu(@Nullable MenuType<?> type, int id, Player player)
    {
        super(type, id);
        this.player = player;
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

    public void addPlayerInventorySlots(Inventory inventory)
    {
        addPlayerInventorySlots(inventory, 16, 92);
    }

    public void addPlayerInventorySlots(Inventory inventory, int x, int y)
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

    public void handleVariable(int index, FriendlyByteBuf buffer)
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

            if (variable.canBeUpdated() && player instanceof ServerPlayer serverPlayer)
                ZYCraft.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new UpdateMenuDataPacket(containerId, i, variable));
        }
    }

    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
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
