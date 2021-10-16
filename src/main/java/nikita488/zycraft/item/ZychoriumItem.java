package nikita488.zycraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
    public boolean canChangeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int rgb)
    {
        int red = (rgb >> 16) & 255;
        int green = (rgb >> 8) & 255;
        int blue = rgb & 255;
        boolean sneaking = player.isShiftKeyDown();

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
    public int changeColor(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, int rgb)
    {
        int rgba = Color.rgba(rgb, 255);
        boolean sneaking = player.isShiftKeyDown();

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

                return Color.hsvToRGB(hsv[0], hsv[1], Mth.clamp(brightness, 0F, 1F));
            case LIGHT:
                return sneaking ? 0x080808 : 0xFFFFFF;
        }

        return rgb;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader reader, BlockPos pos, Player player)
    {
        return IColorable.isColorable(reader, pos);
    }
}
