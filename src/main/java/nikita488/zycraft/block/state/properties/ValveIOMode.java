package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum ValveIOMode implements IStringSerializable
{
    IN(0x204090),
    OUT(0xDD8000);

    public static final ValveIOMode[] VALUES = values();
    private final String name;
    private final int rgb;

    ValveIOMode(int rgb)
    {
        this.name = name().toLowerCase(Locale.ROOT);
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

    @Override
    public String getString()
    {
        return name;
    }
}
