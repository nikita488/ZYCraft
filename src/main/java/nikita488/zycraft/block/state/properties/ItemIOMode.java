package nikita488.zycraft.block.state.properties;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import nikita488.zycraft.init.ZYLang;

public enum ItemIOMode implements StringRepresentable
{
    ANY("any", 0xFFFFFF),
    ALL_IN("all_in", 0x204090),
    ALL_OUT("all_out", 0xDD8000),
    IN1("in1", 0x7000CC),
    OUT1("out1", 0xBB0000),
    IN2("in2", 0x308000),
    OUT2("out2", 0xAAAA00);

    public static final ItemIOMode[] VALUES = values();
    public static final ItemIOMode[] IO_VALUES = new ItemIOMode[] { ALL_IN, ALL_OUT, IN1, OUT1, IN2, OUT2 };
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

    public TranslatableComponent displayName()
    {
        return switch (this)
        {
            case ANY -> ZYLang.ITEM_IO_ANY;
            case ALL_IN -> ZYLang.ITEM_IO_ALL_IN;
            case ALL_OUT -> ZYLang.ITEM_IO_ALL_OUT;
            case IN1 -> ZYLang.ITEM_IO_IN1;
            case OUT1 -> ZYLang.ITEM_IO_OUT1;
            case IN2 -> ZYLang.ITEM_IO_IN2;
            case OUT2 -> ZYLang.ITEM_IO_OUT2;
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
