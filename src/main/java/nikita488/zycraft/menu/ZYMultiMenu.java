package nikita488.zycraft.menu;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import nikita488.zycraft.menu.slot.IOSlotOverlay;
import nikita488.zycraft.menu.slot.SlotIOMode;
import nikita488.zycraft.multiblock.MultiBlock;

import javax.annotation.Nullable;

public class ZYMultiMenu<M extends MultiBlock> extends ZYMenu
{
    public static final int SLOT_OVERLAY_SIZE = 20;
    @Nullable
    protected final M multiBlock;
    private final ObjectList<IOSlotOverlay> slotOverlays = new ObjectArrayList<>();

    public ZYMultiMenu(@Nullable MenuType<?> type, int id, Player player, @Nullable M multiBlock)
    {
        super(type, id, player);
        this.multiBlock = multiBlock;
    }

    protected Slot addIOSlot(SlotIOMode mode, Slot slot)
    {
        return addIOSlot(mode, SLOT_OVERLAY_SIZE, SLOT_OVERLAY_SIZE, slot);
    }

    protected Slot addIOSlot(SlotIOMode mode, int width, int height, Slot slot)
    {
        if (multiBlock == null || multiBlock.isClientSide())
            slotOverlays.add(new IOSlotOverlay(slot.x, slot.y, width, height, mode));
        return addSlot(slot);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return multiBlock != null && multiBlock.isValid();
    }

    public ObjectList<IOSlotOverlay> slotOverlays()
    {
        return slotOverlays;
    }

}