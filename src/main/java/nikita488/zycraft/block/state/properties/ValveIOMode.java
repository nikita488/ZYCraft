package nikita488.zycraft.block.state.properties;

import net.minecraft.util.IStringSerializable;

public enum ValveIOMode implements IStringSerializable
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

    @Override
    public String getString()
    {
        return name;
    }
}
