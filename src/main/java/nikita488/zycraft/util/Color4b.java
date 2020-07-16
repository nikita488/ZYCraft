package nikita488.zycraft.util;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;

public class Color4b
{
    protected byte r, g, b, a;

    public static Color4b from(int r, int g, int b, int a)
    {
        return new Color4b().set(r, g, b, a);
    }

    public static Color4b from(int rgba)
    {
        return new Color4b().set(rgba);
    }

    public static Color4b from(int rgb, int a)
    {
        return new Color4b().set(rgb, a);
    }

    public Color4b set(int rgba)
    {
        return set((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
    }

    public Color4b set(int rgb, int a)
    {
        return set(rgb << 8 | (a & 0xFF));
    }

    public Color4b set(int r, int g, int b, int a)
    {
        return setR(r).setG(g).setB(b).setA(a);
    }

    public Color4b add(int r, int g, int b, int a)
    {
        return set(r() + r, g() + g, b() + b, a() + a);
    }

    public Color4b subtract(int r, int g, int b, int a)
    {
        return add(-r, -g, -b, -a);
    }

    public Color4b multiply(float amount)
    {
        return multiply(amount, amount, amount, amount);
    }

    public Color4b multiplyRGB(float amount)
    {
        return multiply(amount, amount, amount, 1);
    }

    public Color4b multiply(float r, float g, float b, float a)
    {
        return set((int)(r() * r), (int)(g() * g), (int)(b() * b), (int)(a() * a));
    }

    public int rgba()
    {
        return rgba(r, g, b, a);
    }

    public int rgb()
    {
        return rgb(r, g, b);
    }

    public static int rgba(int r, int g, int b, int a)
    {
        return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | a & 0xFF;
    }

    public static int rgba(int rgb, int a)
    {
        return rgba(rgb, a, false);
    }

    public static int rgba(int rgb, int a, boolean argb)
    {
        return argb ? (a & 0xFF) << 24 | rgb : rgb << 8 | (a & 0xFF);
    }

    public static int rgb(int r, int g, int b)
    {
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    private static int mix(int x, int y, float a)
    {
        return (int)(x * (1.0F - a) + y * a);
    }

    public static Color4b[] gradient(Color4b start, Color4b end, int steps)
    {
        Color4b[] colors = new Color4b[steps];
        steps -= 1;

        colors[0] = start;
        colors[steps] = end;

        for (int i = 1; i < steps; i++)
        {
            float amount = (float)i / steps;
            int r = mix(start.r(), end.r(), amount);
            int g = mix(start.g(), end.g(), amount);
            int b = mix(start.b(), end.b(), amount);
            int a = mix(start.a(), end.a(), amount);

            colors[i] = Color4b.from(r, g, b, a);
        }

        return colors;
    }

    public static Color4b load(CompoundNBT nbt)
    {
        return Color4b.from(nbt.getInt("Color"));
    }

    public static Color4b loadRGB(CompoundNBT nbt)
    {
        return Color4b.from(nbt.getInt("Color"), 255);
    }

    public Color4b save(CompoundNBT nbt)
    {
        nbt.putInt("Color", rgba());
        return this;
    }

    public Color4b saveRGB(CompoundNBT nbt)
    {
        nbt.putInt("Color", rgb());
        return this;
    }

    public static Color4b decode(PacketBuffer buffer)
    {
        return Color4b.from(buffer.readVarInt());
    }

    public static Color4b decodeRGB(PacketBuffer buffer)
    {
        return Color4b.from(buffer.readVarInt(), 255);
    }

    public Color4b encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(rgba());
        return this;
    }

    public Color4b encodeRGB(PacketBuffer buffer)
    {
        buffer.writeVarInt(rgb());
        return this;
    }

    public int r()
    {
        return r & 0xFF;
    }

    public int g()
    {
        return g & 0xFF;
    }

    public int b()
    {
        return b & 0xFF;
    }

    public int a()
    {
        return a & 0xFF;
    }

    public Color4b setR(int value)
    {
        r = (byte)MathHelper.clamp(value, 0, 255);
        return this;
    }

    public Color4b setG(int value)
    {
        g = (byte)MathHelper.clamp(value, 0, 255);
        return this;
    }

    public Color4b setB(int value)
    {
        b = (byte)MathHelper.clamp(value, 0, 255);
        return this;
    }

    public Color4b setA(int value)
    {
        a = (byte)MathHelper.clamp(value, 0, 255);
        return this;
    }

    public static int component(int color, int componentIndex)
    {
        switch (componentIndex)
        {
            case 0:
                return (color >> 24) & 0xFF;
            case 1:
                return (color >> 16) & 0xFF;
            case 2:
                return (color >> 8) & 0xFF;
            case 3:
                return color & 0xFF;
        }

        return -1;
    }

    public Color4b copy()
    {
        return Color4b.from(rgba());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("R", r())
                .add("G", g())
                .add("B", b())
                .add("A", a())
                .toString();
    }
}
