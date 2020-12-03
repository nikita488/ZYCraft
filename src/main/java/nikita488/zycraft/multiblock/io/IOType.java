package nikita488.zycraft.multiblock.io;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public enum IOType
{
    ALL(0xFFFFFF),
    ALL_IN(0x204090),
    ALL_OUT(0xDD8000),
    IN1(0x7000CC),
    OUT1(0xBB0000),
    IN2(0x308000),
    OUT2(0xAAAA00);

    public static final IOType[] VALUES = values();
    private final int rgb;

    IOType(int rgb)
    {
        this.rgb = rgb;
    }

    public IOType next()
    {
        switch (this)
        {
            case ALL:
                return ALL_IN;
            case ALL_IN:
                return ALL_OUT;
            case ALL_OUT:
                return IN1;
            case IN1:
                return OUT1;
            case OUT1:
                return IN2;
            case IN2:
                return OUT2;
            case OUT2:
                return ALL;
            default:
                return this;
        }
    }

    public IOType previous()
    {
        switch (this)
        {
            case ALL:
                return OUT2;
            case ALL_IN:
                return ALL;
            case ALL_OUT:
                return ALL_IN;
            case IN1:
                return ALL_OUT;
            case OUT1:
                return IN1;
            case IN2:
                return OUT1;
            case OUT2:
                return IN2;
            default:
                return this;
        }
    }

    public static IOType load(CompoundNBT tag)
    {
        return IOType.valueOf(tag.getString("IO"));
    }

    public void save(CompoundNBT tag)
    {
        tag.putString("IO", name());
    }

    public static IOType decode(CompoundNBT tag)
    {
        return VALUES[tag.getByte("IO")];
    }

    public void encode(CompoundNBT tag)
    {
        tag.putByte("IO", (byte)ordinal());
    }

    public static IOType decode(PacketBuffer buffer)
    {
        return buffer.readEnumValue(IOType.class);
    }

    public void encode(PacketBuffer buffer)
    {
        buffer.writeEnumValue(this);
    }

    public boolean isOutput()
    {
        return this == ALL_OUT || this == OUT1 || this == OUT2;
    }

    public int rgb()
    {
        return rgb;
    }

    public int argb(int alpha)
    {
        return alpha << 24 | rgb;
    }
}
