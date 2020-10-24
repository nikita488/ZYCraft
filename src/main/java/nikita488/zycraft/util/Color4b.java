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

    public static int HSBtoRGB(float hue, float saturation, float brightness)
    {
        int r = 0, g = 0, b = 0;
        
        if (saturation <= 0)
        {
            r = g = b = (int)(brightness * 255.0F + 0.5f);
        }
        else
        {
            float h = (hue - (float)Math.floor(hue)) * 6.0F;
            float f = h - (float)Math.floor(h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - (saturation * (1.0F - f)));

            switch ((int)h)
            {
                case 0:
                    r = (int)(brightness * 255.0F + 0.5f);
                    g = (int)(t * 255.0F + 0.5f);
                    b = (int)(p * 255.0F + 0.5f);
                    break;
                case 1:
                    r = (int)(q * 255.0F + 0.5f);
                    g = (int)(brightness * 255.0F + 0.5f);
                    b = (int)(p * 255.0F + 0.5f);
                    break;
                case 2:
                    r = (int)(p * 255.0F + 0.5f);
                    g = (int)(brightness * 255.0F + 0.5f);
                    b = (int)(t * 255.0F + 0.5f);
                    break;
                case 3:
                    r = (int)(p * 255.0F + 0.5f);
                    g = (int)(q * 255.0F + 0.5f);
                    b = (int)(brightness * 255.0F + 0.5f);
                    break;
                case 4:
                    r = (int)(t * 255.0F + 0.5f);
                    g = (int)(p * 255.0F + 0.5f);
                    b = (int)(brightness * 255.0F + 0.5f);
                    break;
                case 5:
                    r = (int)(brightness * 255.0F + 0.5f);
                    g = (int)(p * 255.0F + 0.5f);
                    b = (int)(q * 255.0F + 0.5f);
                    break;
            }
        }
        
        return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
    }

    public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals)
    {
        float hue, saturation, brightness;

        if (hsbvals == null)
        {
            hsbvals = new float[3];
        }

        int cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        int cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((float)cmax) / 255.0F;

        if (cmax != 0)
            saturation = ((float)(cmax - cmin)) / ((float)cmax);
        else
            saturation = 0;

        if (saturation <= 0)
            hue = 0;
        else
        {
            float redc = ((float)(cmax - r)) / ((float)(cmax - cmin));
            float greenc = ((float)(cmax - g)) / ((float)(cmax - cmin));
            float bluec = ((float)(cmax - b)) / ((float)(cmax - cmin));

            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;

            hue = hue / 6.0f;

            if (hue < 0)
                hue = hue + 1.0f;
        }

        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;

        return hsbvals;
    }

    public static Color4b load(CompoundNBT tag)
    {
        return Color4b.from(tag.getInt("Color"));
    }

    public static Color4b loadRGB(CompoundNBT tag)
    {
        return Color4b.from(tag.getInt("Color"), 255);
    }

    public Color4b save(CompoundNBT tag)
    {
        tag.putInt("Color", rgba());
        return this;
    }

    public Color4b saveRGB(CompoundNBT tag)
    {
        tag.putInt("Color", rgb());
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
