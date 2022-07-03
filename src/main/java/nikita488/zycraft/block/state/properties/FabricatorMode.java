package nikita488.zycraft.block.state.properties;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.init.ZYLang;

public enum FabricatorMode implements StringRepresentable
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
        return switch (this)
        {
            case AUTO_LOW -> !powered;
            case AUTO_HIGH -> powered;
            case PULSE -> !lastPowered && powered;
        };
    }

    public int rgb(boolean powered)
    {
        if (this == AUTO_LOW)
            return powered ? 0x002142 : ZYType.BLUE.rgb();
        return powered ? ZYType.BLUE.rgb() : 0x002142;
    }

    public MutableComponent displayName()
    {
        return switch (this)
        {
            case AUTO_HIGH -> ZYLang.FABRICATOR_AUTO_HIGH;
            case AUTO_LOW -> ZYLang.FABRICATOR_AUTO_LOW;
            case PULSE -> ZYLang.FABRICATOR_PULSE;
        };
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
