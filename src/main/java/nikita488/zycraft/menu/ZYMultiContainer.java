package nikita488.zycraft.menu;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import nikita488.zycraft.menu.slot.IOSlotOverlay;
import nikita488.zycraft.menu.slot.SlotIOMode;
import nikita488.zycraft.multiblock.MultiBlock;

import javax.annotation.Nullable;

public class ZYMultiContainer<M extends MultiBlock> extends ZYContainer
{
    public static final int SLOT_OVERLAY_SIZE = 20;
    @Nullable
    protected final M multiBlock;
    private final ObjectList<IOSlotOverlay> slotOverlays = new ObjectArrayList<>();

    public ZYMultiContainer(@Nullable ContainerType<?> type, int windowID, @Nullable M multiBlock)
    {
        super(type, windowID);
        this.multiBlock = multiBlock;
    }

    protected Slot addIOSlot(SlotIOMode mode, Slot slot)
    {
        return addIOSlot(mode, SLOT_OVERLAY_SIZE, SLOT_OVERLAY_SIZE, slot);
    }

    protected Slot addIOSlot(SlotIOMode mode, int width, int height, Slot slot)
    {
        if (multiBlock == null || multiBlock.isClientSide())
            slotOverlays.add(new IOSlotOverlay(slot.xPos, slot.yPos, width, height, mode));
        return addSlot(slot);
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return multiBlock != null && multiBlock.isValid();
    }

    public ObjectList<IOSlotOverlay> slotOverlays()
    {
        return slotOverlays;
    }

}
