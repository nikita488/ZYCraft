package nikita488.zycraft.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockDisplayReader;
import nikita488.zycraft.block.ZychoriumLampBlock;
import nikita488.zycraft.util.Color;

public class ZychoriumLampTile extends ColorableTile
{
    private Vector3f hsv = new Vector3f(0, 0, 1);

    public ZychoriumLampTile(TileEntityType<ZychoriumLampTile> type)
    {
        super(type);
    }

    @Override
    public int getColor(BlockState state, IBlockDisplayReader world, BlockPos pos, int tintIndex)
    {
        return Color.hsvToRGB(hsv.getX(), hsv.getY(), hsv.getZ() * brightness(state));
    }

    protected float brightness(BlockState state)
    {
        int strength = world.getRedstonePowerFromNeighbors(pos);
        boolean inverted = ((ZychoriumLampBlock)state.getBlock()).inverted();
        return 0.25F + 0.05F * (inverted ? 15 - strength : strength);
    }

    @Override
    public void decode(CompoundNBT tag)
    {
        super.decode(tag);
        this.hsv = Color.rgbToHSV(color.red(), color.green(), color.blue());
    }
}
