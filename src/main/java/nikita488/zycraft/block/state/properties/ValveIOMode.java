package nikita488.zycraft.block.state.properties;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import nikita488.zycraft.init.ZYLang;

public enum ValveIOMode implements StringRepresentable
{
    IN("in", 0x204090),
    OUT("out", 0xDD8000);

    public static final ValveIOMode[] VALUES = values();
    private final String name;
    private final int rgb;

    ValveIOMode(String name, int rgb)
    {
        this.name = name;
        this.rgb = rgb;
    }

    public boolean isOutput()
    {
        return this == OUT;
    }

    public int rgb()
    {
        return rgb;
    }

    public TranslatableComponent displayName()
    {
        switch (this)
        {
            case IN:
            default:
                return ZYLang.VALVE_IN;
            case OUT:
                return ZYLang.VALVE_OUT;
        }
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
