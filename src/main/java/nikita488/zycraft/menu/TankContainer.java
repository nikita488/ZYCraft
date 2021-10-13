package nikita488.zycraft.menu;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.menu.data.FluidMenuData;
import nikita488.zycraft.menu.data.IOMenuData;
import nikita488.zycraft.menu.slot.FluidHandlerSlot;
import nikita488.zycraft.menu.slot.SlotIOMode;
import nikita488.zycraft.menu.slot.ZYSlot;
import nikita488.zycraft.multiblock.TankMultiBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankContainer extends ZYMultiContainer<TankMultiBlock>
{
    public static final int INPUT_SLOT = TankMultiBlock.INPUT_SLOT;
    public static final int OUTPUT_SLOT = TankMultiBlock.OUTPUT_SLOT;
    private final FluidMenuData fluidData;
    private final int capacity;
    private final IOMenuData ioData;

    public TankContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, PacketBuffer buffer)
    {
        this(type, windowID, playerInventory, null, new ItemStackHandler(2), new FluidMenuData(), buffer.readVarInt(), new IOMenuData());
    }

    public TankContainer(int windowID, PlayerInventory playerInventory, TankMultiBlock tank)
    {
        this(ZYContainers.TANK.get(), windowID, playerInventory, tank, tank.inventory(), new FluidMenuData(() -> tank.fluidTank().getFluid()), tank.fluidTank().getCapacity(), new IOMenuData(tank::inventory));
    }

    public TankContainer(@Nullable ContainerType<?> type, int windowID, PlayerInventory playerInventory, @Nullable TankMultiBlock multiBlock, IItemHandler inventory, FluidMenuData fluidData, int capacity, IOMenuData ioData)
    {
        super(type, windowID, multiBlock);

        this.fluidData = fluidData;
        this.capacity = capacity;
        this.ioData = ioData;

        addVariable(fluidData);
        addVariable(ioData);
        assertInventorySize(inventory, 2);
        assertDataSize(data, 2);

        addIOSlot(SlotIOMode.IN1, new FluidHandlerSlot(inventory, INPUT_SLOT, 97, 53));
        addIOSlot(SlotIOMode.OUT1, new ZYSlot(inventory, OUTPUT_SLOT, 151, 53)
        {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack)
            {
                return false;
            }
        });
        addPlayerInventorySlots(playerInventory, 16, 98);
    }

    @Override
    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return slotIndex <= 1 ? moveItemStackTo(stack, 2, 38, false) : moveItemStackTo(stack, INPUT_SLOT, OUTPUT_SLOT, true);
    }

    public FluidMenuData fluidData()
    {
        return fluidData;
    }

    public int capacity()
    {
        return capacity;
    }

    public IOMenuData ioData()
    {
        return ioData;
    }
}
