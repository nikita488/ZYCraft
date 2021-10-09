package nikita488.zycraft.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nikita488.zycraft.api.colorable.IColorChanger;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.Color;

public class ZychoriumItem extends Item implements IColorChanger
{
    private final ZYType type;

    public ZychoriumItem(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean canChangeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int rgb)
    {
        int red = (rgb >> 16) & 255;
        int green = (rgb >> 8) & 255;
        int blue = rgb & 255;
        boolean sneaking = player.isSneaking();

        switch (type)
        {
            case RED:
                return canChangeComponent(red, sneaking);
            case GREEN:
                return canChangeComponent(green, sneaking);
            case BLUE:
                return canChangeComponent(blue, sneaking);
            case DARK:
                float brightness = Color.rgbToHSV(red, green, blue)[2];
                return sneaking ? brightness > 0F : brightness < 1F;
            case LIGHT:
                return sneaking ? rgb != 0x080808 : rgb != -1;
        }

        return false;
    }

    private boolean canChangeComponent(int value, boolean sneaking)
    {
        return value != (sneaking ? 0 : 255);
    }

    @Override
    public int changeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int rgb)
    {
        int rgba = Color.rgba(rgb, 255);
        boolean sneaking = player.isSneaking();

        switch (type)
        {
            case RED:
                return Color.rgb(sneaking ? Color.subtract(rgba, 0x08000000) : Color.add(rgba, 0x08000000));
            case GREEN:
                return Color.rgb(sneaking ? Color.subtract(rgba, 0x00080000) : Color.add(rgba, 0x00080000));
            case BLUE:
                return Color.rgb(sneaking ? Color.subtract(rgba, 0x00000800) : Color.add(rgba, 0x00000800));
            case DARK:
                float[] hsv = Color.rgbToHSV((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
                float brightness = hsv[2];

                if (!sneaking)
                    brightness += (8 / 255F);
                else
                    brightness -= (8 / 255F);

                return Color.hsvToRGB(hsv[0], hsv[1], MathHelper.clamp(brightness, 0F, 1F));
            case LIGHT:
                return sneaking ? 0x080808 : 0xFFFFFF;
        }

        return rgb;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return IColorable.isColorable(world, pos);
    }
}
