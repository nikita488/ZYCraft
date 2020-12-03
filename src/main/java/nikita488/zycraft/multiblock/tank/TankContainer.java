package nikita488.zycraft.multiblock.tank;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nikita488.zycraft.container.FluidContainerSlot;
import nikita488.zycraft.container.FluidContainerVar;
import nikita488.zycraft.container.OutputSlot;
import nikita488.zycraft.init.ZYContainers;
import nikita488.zycraft.multiblock.container.MultiContainer;

public class TankContainer extends MultiContainer<TankMultiBlock>
{
    private final FluidContainerVar fluidVar;
    private final int capacity;

    public TankContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, PacketBuffer buffer)
    {
        this(type, windowID, playerInventory, null, new ItemStackHandler(2), new FluidContainerVar(), buffer.readVarInt());
    }

    public TankContainer(int windowID, PlayerInventory playerInventory, TankMultiBlock tank)
    {
        this(ZYContainers.TANK.get(), windowID, playerInventory, tank, tank.getInventory(), new FluidContainerVar(() -> tank.getFluidTank().getFluid()), tank.getFluidTank().getCapacity());
    }

    public TankContainer(ContainerType<?> type, int windowID, PlayerInventory playerInventory, TankMultiBlock tank, IItemHandler inventory, FluidContainerVar fluidVar, int capacity)
    {
        super(type, windowID, tank);

        addContainerVar(fluidVar);
        assertInventorySize(inventory, 2);
        addSlot(new FluidContainerSlot(inventory, 0, 98, 47));
        addSlot(new OutputSlot(inventory, 1, 151, 47));
        addPlayerInventorySlots(playerInventory, 18, 131);
        this.fluidVar = fluidVar;
        this.capacity = capacity;
    }

    @Override
    public boolean tryTransferStackToSlot(ItemStack stack, int slotIndex)
    {
        return slotIndex <= 1 ? mergeItemStack(stack, 2, 38, false) : mergeItemStack(stack, 0, 1, true);
    }

    public FluidStack fluid()
    {
        return fluidVar.fluid();
    }

    public int capacity()
    {
        return capacity;
    }
}
