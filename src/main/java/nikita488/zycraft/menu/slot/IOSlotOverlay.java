package nikita488.zycraft.menu.slot;

import nikita488.zycraft.block.state.properties.ItemIOMode;

public class IOSlotOverlay
{
    private final int x, y, width, height;
    private final SlotIOMode mode;

    public IOSlotOverlay(int x, int y, int width, int height, SlotIOMode mode)
    {
        this.x = x;
        this.y = y;
        this.width = Math.max(width, 16);
        this.height = Math.max(height, 16);
        this.mode = mode;
    }

    public int x()
    {
        return x;
    }

    public int y()
    {
        return y;
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    public boolean canBeRendered(ItemIOMode mode)
    {
        return this.mode.isSupported(mode);
    }
}
