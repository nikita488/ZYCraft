package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;

public enum ItemIOMode implements IStringSerializable
{
    ALL("all", 0xFFFFFF),
    ALL_IN("all_in", 0x204090),
    ALL_OUT("all_out", 0xDD8000),
    IN1("in1", 0x7000CC),
    OUT1("out1", 0xBB0000),
    IN2("in2", 0x308000),
    OUT2("out2", 0xAAAA00);

    public static final ItemIOMode[] VALUES = values();
    private final String name;
    private final int rgb;

    ItemIOMode(String name, int rgb)
    {
        this.name = name;
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
