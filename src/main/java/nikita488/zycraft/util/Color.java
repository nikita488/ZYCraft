package nikita488.zycraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Color
{
    private final int color;

    private Color(int rgba)
    {
        this.color = rgba;
    }

    public static Color fromRGB(int rgb)
    {
        return fromRGBA(rgb, 255);
    }

    public static Color fromRGB(int r, int g, int b)
    {
        return fromRGBA(r, g, b, 255);
    }

    public static Color fromRGBA(int rgb, int a)
    {
        return fromRGBA(rgba(rgb, a));
    }

    public static Color fromRGBA(int r, int g, int b, int a)
    {
        return fromRGBA(rgba(r, g, b, a));
    }

    public static Color fromARGB(int argb)
    {
        return fromRGBA(rgba(argb));
    }

    public static Color fromRGBA(int rgba)
    {
        return new Color(rgba);
    }

    public Color add(int r, int g, int b, int a)
    {
        r = clamp(red() + r);
        g = clamp(green() + g);
        b = clamp(blue() + b);
        a = clamp(alpha() + a);

        return fromRGBA(r, g, b, a);
    }

    public Color subtract(int r, int g, int b, int a)
    {
        return add(-r, -g, -b, -a);
    }

    public Color multiplyRGB(float amount)
    {
        return multiply(amount, amount, amount, 1F);
    }

    public Color multiply(float amount)
    {
        return multiply(amount, amount, amount, amount);
    }

    public Color multiply(float r, float g, float b, float a)
    {
        r = clamp(red() * r);
        g = clamp(green() * g);
        b = clamp(blue() * b);
        a = clamp(alpha() * a);

        return fromRGBA((int)r, (int)g, (int)b, (int)a);
    }

    public static Color load(CompoundTag tag)
    {
        return fromRGBA(tag.getInt("Color"));
    }

    public Color save(CompoundTag tag)
    {
        tag.putInt("Color", color);
        return this;
    }

    public static Color decode(FriendlyByteBuf buffer)
    {
        return fromRGBA(buffer.readVarInt());
    }

    public Color encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(color);
        return this;
    }

    public int rgb()
    {
        return rgb(color);
    }

    public int rgba()
    {
        return color;
    }

    public int argb()
    {
        return argb(color);
    }

    public int red()
    {
        return wrap(color >> 24);
    }

    public int green()
    {
        return wrap(color >> 16);
    }

    public int blue()
    {
        return wrap(color >> 8);
    }

    public int alpha()
    {
        return wrap(color);
    }

    public static int add(int rgba1, int rgba2)
    {
        int r = clamp(wrap(rgba1 >> 24) + wrap(rgba2 >> 24));
        int g = clamp(wrap(rgba1 >> 16) + wrap(rgba2 >> 16));
        int b = clamp(wrap(rgba1 >> 8) + wrap(rgba2 >> 8));
        int a = clamp(wrap(rgba1) + wrap(rgba2));

        return rgba(r, g, b, a);
    }

    public static int subtract(int rgba1, int rgba2)
    {
        int r = clamp(wrap(rgba1 >> 24) - wrap(rgba2 >> 24));
        int g = clamp(wrap(rgba1 >> 16) - wrap(rgba2 >> 16));
        int b = clamp(wrap(rgba1 >> 8) - wrap(rgba2 >> 8));
        int a = clamp(wrap(rgba1) - wrap(rgba2));

        return rgba(r, g, b, a);
    }

    public static int multiplyRGBBy(int rgba, float amount)
    {
        float r = clamp(wrap(rgba >> 24) * amount);
        float g = clamp(wrap(rgba >> 16) * amount);
        float b = clamp(wrap(rgba >> 8) * amount);
        int a = wrap(rgba);

        return rgba((int)r, (int)g, (int)b, a);
    }

    public static int multiplyBy(int rgba, float amount)
    {
        float r = clamp(wrap(rgba >> 24) * amount);
        float g = clamp(wrap(rgba >> 16) * amount);
        float b = clamp(wrap(rgba >> 8) * amount);
        float a = clamp(wrap(rgba) * amount);

        return rgba((int)r, (int)g, (int)b, (int)a);
    }

    public static int rgb(int rgba)
    {
        return (rgba & 0xFFFFFF00) >> 8;
    }

    public static int rgb(int r, int g, int b)
    {
        return wrap(r) << 16 | wrap(g) << 8 | wrap(b);
    }

    public static int rgba(int rgb, int a)
    {
        return (rgb & 0xFFFFFF) << 8 | wrap(a);
    }

    public static int rgba(int argb)
    {
        return rgba(argb & 0xFFFFFF, argb >> 24);
    }

    public static int rgba(int r, int g, int b, int a)
    {
        return rgb(r, g, b) << 8 | wrap(a);
    }

    public static int argb(int rgba)
    {
        return wrap(rgba) << 24 | rgb(rgba);
    }

    public static int argb(int rgb, int a)
    {
        return wrap(a) << 24 | (rgb & 0xFFFFFF);
    }

    public static int argb(int r, int g, int b, int a)
    {
        return wrap(a) << 24 | rgb(r, g, b);
    }

    public static float[] rgbToHSV(int r, int g, int b)
    {
        int min = Math.min(b, Math.min(r, g));
        int max = Math.max(b, Math.max(r, g));
        float diff = max - min;

        float h = 0;
        float s = max != 0 ? diff / max : 0F;
        float v = max / 255F;

        if (s > 0F)
        {
            float red = (max - r) / diff;
            float green = (max - g) / diff;
            float blue = (max - b) / diff;

            if (r == max)
                h = blue - green;
            else if (g == max)
                h = 2 + red - blue;
            else
                h = 4 + green - red;

            h /= 6F;

            if (h < 0F)
                h += 1F;
        }

        return new float[] {h, s, v};
    }

    public static int hsvToRGB(float h, float s, float v)
    {
        return Mth.hsvToRgb(h, s, v);
    }

    private static int wrap(int value)
    {
        return value & 255;
    }

    private static int clamp(int value)
    {
        return Mth.clamp(value, 0, 255);
    }

    private static float clamp(float value)
    {
        return Mth.clamp(value, 0, 255);
    }
}
