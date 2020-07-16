package nikita488.zycraft.item;

import nikita488.zycraft.api.colorable.IColorChanger;
import nikita488.zycraft.block.ColorableBlock;
import nikita488.zycraft.enums.ZyType;
import nikita488.zycraft.util.Color4b;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ZychoriumItem extends ZyItem implements IColorChanger
{
    public ZychoriumItem(ZyType type, Properties properties)
    {
        super(type, properties);
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

        switch (type)
        {
            case RED:
                if (!player.isCrouching())
                    rgba.add(8, 0, 0, 0);
                else
                    rgba.subtract(8, 0, 0, 0);
                break;
            case GREEN:
                if (!player.isCrouching())
                    rgba.add(0, 8, 0, 0);
                else
                    rgba.subtract(0, 8, 0, 0);
                break;
            case BLUE:
                if (!player.isCrouching())
                    rgba.add(0, 0, 8, 0);
                else
                    rgba.subtract(0, 0, 8, 0);
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
