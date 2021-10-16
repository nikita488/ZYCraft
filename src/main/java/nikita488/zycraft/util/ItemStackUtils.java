package nikita488.zycraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants;

public class ItemStackUtils
{
    public static ItemStack copy(ItemStack stack, int size)
    {
        if (stack.isEmpty() || size <= 0)
            return ItemStack.EMPTY;

        stack = stack.copy();
        stack.setCount(size);
        return stack;
    }

    public static byte getByte(ItemStack stack, String key, byte defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_BYTE) ? stack.getTag().getByte(key) : defaultValue;
    }

    public static short getShort(ItemStack stack, String key, short defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_SHORT) ? stack.getTag().getShort(key) : defaultValue;
    }

    public static int getInt(ItemStack stack, String key, int defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_INT) ? stack.getTag().getInt(key) : defaultValue;
    }

    public static long getLong(ItemStack stack, String key, long defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_LONG) ? stack.getTag().getLong(key) : defaultValue;
    }

    public static float getFloat(ItemStack stack, String key, float defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_FLOAT) ? stack.getTag().getFloat(key) : defaultValue;
    }

    public static double getDouble(ItemStack stack, String key, double defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_DOUBLE) ? stack.getTag().getDouble(key) : defaultValue;
    }

    public static String getString(ItemStack stack, String key, String defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_STRING) ? stack.getTag().getString(key) : defaultValue;
    }

    public static byte[] getByteArray(ItemStack stack, String key, byte[] defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_BYTE_ARRAY) ? stack.getTag().getByteArray(key) : defaultValue;
    }

    public static int[] getIntArray(ItemStack stack, String key, int[] defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_INT_ARRAY) ? stack.getTag().getIntArray(key) : defaultValue;
    }

    public static long[] getLongArray(ItemStack stack, String key, long[] defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_LONG_ARRAY) ? stack.getTag().getLongArray(key) : defaultValue;
    }

    public static CompoundTag getCompound(ItemStack stack, String key, CompoundTag defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_COMPOUND) ? stack.getTag().getCompound(key) : defaultValue;
    }

    public static ListTag getList(ItemStack stack, String key, int type, ListTag defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_LIST) ? stack.getTag().getList(key, type) : defaultValue;
    }

    public static boolean getBoolean(ItemStack stack, String key, boolean defaultValue)
    {
        if (!stack.hasTag())
            return defaultValue;
        return stack.getTag().contains(key, Constants.NBT.TAG_BYTE) ? stack.getTag().getBoolean(key) : defaultValue;
    }
}
