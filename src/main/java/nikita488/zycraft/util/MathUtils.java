package nikita488.zycraft.util;

import net.minecraft.util.math.MathHelper;

public class MathUtils
{
    public static int lerp(float ratio, int a, int b)
    {
        int value = Math.round(MathHelper.lerp(ratio, a, b));
        return value == a ? b : value;
    }
}
