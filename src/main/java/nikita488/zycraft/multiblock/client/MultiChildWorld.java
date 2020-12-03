package nikita488.zycraft.multiblock.client;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.lighting.WorldLightManager;
import nikita488.zycraft.multiblock.tile.DefaultMultiChildTile;

import javax.annotation.Nullable;

public class MultiChildWorld implements IBlockDisplayReader
{
    private final IBlockDisplayReader parent;

    public MultiChildWorld(IBlockDisplayReader parent)
    {
        this.parent = parent;
    }

    @Override
    public BlockState getBlockState(BlockPos pos)
    {
        TileEntity tile = parent.getTileEntity(pos);

        if (tile instanceof DefaultMultiChildTile)
            return ((DefaultMultiChildTile)tile).state();

        return parent.getBlockState(pos);
    }

    @Override
    public float func_230487_a_(Direction side, boolean applyDiffuseLighting)
    {
        return parent.func_230487_a_(side, applyDiffuseLighting);
    }

    @Override
    public WorldLightManager getLightManager()
    {
        return parent.getLightManager();
    }

    @Override
    public int getBlockColor(BlockPos pos, ColorResolver resolver)
    {
        return parent.getBlockColor(pos, resolver);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos)
    {
        return parent.getTileEntity(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos)
    {
        return parent.getFluidState(pos);
    }
}