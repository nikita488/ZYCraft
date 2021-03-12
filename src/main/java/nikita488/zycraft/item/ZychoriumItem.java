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
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.util.Color4b;

import net.minecraft.item.Item.Properties;

public class ZychoriumItem extends Item implements IColorChanger
{
    private final ZYType type;

    public ZychoriumItem(ZYType type, Properties properties)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public boolean canChangeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        Color4b rgba = Color4b.from(color, 255);
        boolean sneaking = player.isCrouching();

        switch (type)
        {
            case RED:
                return canChangeComponent(rgba.r(), sneaking);
            case GREEN:
                return canChangeComponent(rgba.g(), sneaking);
            case BLUE:
                return canChangeComponent(rgba.b(), sneaking);
            case DARK:
                float[] hsb = new float[3];
                Color4b.RGBtoHSB(rgba.r(), rgba.g(), rgba.b(), hsb);

                float brightness = hsb[2];
                return sneaking ? brightness > 0.0F : brightness < 1.0F;
            case LIGHT:
                return sneaking ? color != 0x080808 : color != 0xFFFFFF;
        }

        return false;
    }

    private boolean canChangeComponent(int value, boolean sneaking)
    {
        return sneaking ? value != 0 : value != 255;
    }

    @Override
    public int changeColor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, int color)
    {
        Color4b rgba = Color4b.from(color, 255);
        boolean sneaking = player.isCrouching();

        switch (type)
        {
            case RED:
                if (!sneaking)
                    rgba.add(8, 0, 0, 0);
                else
                    rgba.subtract(8, 0, 0, 0);
                break;
            case GREEN:
                if (!sneaking)
                    rgba.add(0, 8, 0, 0);
                else
                    rgba.subtract(0, 8, 0, 0);
                break;
            case BLUE:
                if (!sneaking)
                    rgba.add(0, 0, 8, 0);
                else
                    rgba.subtract(0, 0, 8, 0);
                break;
            case DARK:
                float[] hsb = new float[3];
                Color4b.RGBtoHSB(rgba.r(), rgba.g(), rgba.b(), hsb);

                float brightness = hsb[2];

                if (!sneaking)
                    brightness += 0.03125F;
                else
                    brightness -= 0.03125F;

                rgba.set(Color4b.HSBtoRGB(hsb[0], hsb[1], MathHelper.clamp(brightness, 0.0F, 1.0F)), 255);
                break;
            case LIGHT:
                if (!sneaking)
                    rgba.set(0xFFFFFF, 255);
                else
                    rgba.set(0x080808, 255);
                break;
        }

        return rgba.rgb();
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return world.getBlockState(pos).getBlock() instanceof ColorableBlock;
    }
}
