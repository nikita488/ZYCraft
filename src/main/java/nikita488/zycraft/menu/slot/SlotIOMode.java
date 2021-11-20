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
        return switch (mode)
        {
            case ALL_IN -> this == IN1 || this == IN2;
            case ALL_OUT -> this == OUT1 || this == OUT2;
            case IN1 -> this == IN1;
            case OUT1 -> this == OUT1;
            case IN2 -> this == IN2;
            case OUT2 -> this == OUT2;
            default -> false;
        };
    }
}
