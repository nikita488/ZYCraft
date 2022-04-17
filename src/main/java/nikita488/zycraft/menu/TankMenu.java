package nikita488.zycraft.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.init.ZYMenus;
import nikita488.zycraft.menu.data.FluidMenuData;
import nikita488.zycraft.menu.data.IOMenuData;
import nikita488.zycraft.menu.slot.FluidHandlerSlot;
import nikita488.zycraft.menu.slot.SlotIOMode;
import nikita488.zycraft.menu.slot.ZYSlot;
import nikita488.zycraft.multiblock.TankMultiBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankMenu extends ZYMultiMenu<TankMultiBlock>
{
    public static final int INPUT_SLOT = TankMultiBlock.INPUT_SLOT;
    public static final int OUTPUT_SLOT = TankMultiBlock.OUTPUT_SLOT;
    private final FluidMenuData fluidData;
    private final int capacity;
    private final IOMenuData ioData;

    public TankMenu(MenuType<?> type, int id, Inventory playerInventory, FriendlyByteBuf buffer)
    {
        this(type, id, playerInventory, null, new ItemStackHandler(2), new FluidMenuData(), buffer.readVarInt(), new IOMenuData());
    }

    public TankMenu(int id, Inventory playerInventory, TankMultiBlock tank)
    {
        this(ZYMenus.TANK.get(), id, playerInventory, tank, tank.inventory(), new FluidMenuData(() -> tank.fluidTank().getFluid()), tank.fluidTank().getCapacity(), new IOMenuData(tank::inventory));
    }

    public TankMenu(@Nullable MenuType<?> type, int id, Inventory playerInventory, @Nullable TankMultiBlock multiBlock, IItemHandler inventory, FluidMenuData fluidData, int capacity, IOMenuData ioData)
    {
        super(type, id, playerInventory.player, multiBlock);

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