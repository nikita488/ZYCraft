package nikita488.zycraft.multiblock.test;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import nikita488.zycraft.block.MultiInterfaceBlock;
import nikita488.zycraft.init.ZYTiles;

import javax.annotation.Nullable;

public class TestBlock extends MultiInterfaceBlock
{
    public TestBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ZYTiles.TEST.create();
    }
}
