package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import nikita488.zycraft.block.ZychoriumLampBlock;
import nikita488.zycraft.util.Color;

public class ZychoriumLampTile extends ColorableTile
{
    private float[] hsv;

    public ZychoriumLampTile(TileEntityType<ZychoriumLampTile> type)
    {
        super(type);
    }

    protected float brightness(boolean inverted)
    {
        int strength = world.getRedstonePowerFromNeighbors(pos);
        return 0.25F + 0.05F * (inverted ? 15 - strength : strength);
    }

    @Override
    public int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintIndex)
    {
        boolean inverted = ((ZychoriumLampBlock)state.getBlock()).inverted();
        int defaultColor = inverted ? 0xFFFFFF : 0x3F3F3F;

        return hsv != null ? Color.hsvToRGB(hsv[0], hsv[1], hsv[2] * brightness(inverted)) : defaultColor;
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        super.decode(tag);
        this.hsv = Color.rgbToHSV(color.red(), color.green(), color.blue());
    }
}
