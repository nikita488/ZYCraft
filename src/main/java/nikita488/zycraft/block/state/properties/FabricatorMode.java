package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TranslationTextComponent;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.init.ZYLang;

public enum FabricatorMode implements IStringSerializable
{
    AUTO_LOW("auto_low"),
    AUTO_HIGH("auto_high"),
    PULSE("pulse");

    public static final FabricatorMode[] VALUES = values();
    private final String name;

    FabricatorMode(String name)
    {
        this.name = name;
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

    public TranslationTextComponent displayName()
    {
        switch (this)
        {
            case AUTO_HIGH:
            default:
                return ZYLang.FABRICATOR_AUTO_HIGH;
            case AUTO_LOW:
                return ZYLang.FABRICATOR_AUTO_LOW;
            case PULSE:
                return ZYLang.FABRICATOR_PULSE;
        }
    }

    @Override
    public String getString()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
