package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum ItemIOMode implements IStringSerializable
{
    ALL(0xFFFFFF),
    ALL_IN(0x204090),
    ALL_OUT(0xDD8000),
    IN1(0x7000CC),
    OUT1(0xBB0000),
    IN2(0x308000),
    OUT2(0xAAAA00);

    public static final ItemIOMode[] VALUES = values();
    private final String name;
    private final int rgb;

    ItemIOMode(int rgb)
    {
        this.name = name().toLowerCase(Locale.ROOT);
        this.rgb = rgb;
    }

    public boolean isOutput()
    {
        return this == ALL_OUT || this == OUT1 || this == OUT2;
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
