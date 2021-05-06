package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;
import nikita488.zycraft.enums.ZYType;

import java.util.Locale;

public enum FabricatorMode implements IStringSerializable
{
    OFF,
    ON,
    PULSE;

    public static final FabricatorMode[] VALUES = values();

    public boolean сanCraft(boolean lastPowered, boolean powered)
    {
        switch (this)
        {
            case OFF:
                return !powered;
            case ON:
                return powered;
            case PULSE:
                return !lastPowered && powered;
            default:
                return false;
        }
    }

    public int rgb(boolean powered)
    {
        if (this == OFF)
            return powered ? 0x00193F : ZYType.BLUE.rgb();
        return powered ? ZYType.BLUE.rgb() : 0x00193F;
    }

    @Override
    public String getString()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
