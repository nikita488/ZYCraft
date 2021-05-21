package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;
import nikita488.zycraft.enums.ZYType;

import java.util.Locale;

public enum FabricatorMode implements IStringSerializable
{
    AUTO_LOW,
    AUTO_HIGH,
    PULSE;

    public static final FabricatorMode[] VALUES = values();
    private final String name;

    FabricatorMode()
    {
        this.name = name().toLowerCase(Locale.ROOT);
    }

    public boolean canCraft(boolean lastPowered, boolean powered)
    {
        switch (this)
        {
            case AUTO_LOW:
                return !powered;
            case AUTO_HIGH:
                return powered;
            case PULSE:
                return !lastPowered && powered;
            default:
                return false;
        }
    }

    public int rgb(boolean powered)
    {
        if (this == AUTO_LOW)
            return powered ? 0x002142 : ZYType.BLUE.rgb();
        return powered ? ZYType.BLUE.rgb() : 0x002142;
    }

    @Override
    public String getString()
    {
        return name;
    }
}
