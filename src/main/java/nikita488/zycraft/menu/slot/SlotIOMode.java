package nikita488.zycraft.menu.slot;

import nikita488.zycraft.block.state.properties.ItemIOMode;

public enum SlotIOMode
{
    IN1,
    OUT1,
    IN2,
    OUT2;

    public boolean isSupported(ItemIOMode mode)
    {
        switch (mode)
        {
            case ALL_IN:
                return this == IN1 || this == IN2;
            case ALL_OUT:
                return this == OUT1 || this == OUT2;
            case IN1:
                return this == IN1;
            case OUT1:
                return this == OUT1;
            case IN2:
                return this == IN2;
            case OUT2:
                return this == OUT2;
            default:
                return false;
        }
    }
}
