package nikita488.zycraft.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import nikita488.zycraft.multiblock.former.IMultiFormer;

import javax.annotation.Nullable;

public class MultiType<T extends MultiBlock> extends ForgeRegistryEntry<MultiType<?>>
{
    private final MultiSupplier<? extends T> factory;
    private final IMultiFormer former;

    public MultiType(MultiSupplier<? extends T> factory, IMultiFormer former)
    {
        this.factory = factory;
        this.former = former;
    }

    @Nullable
    public T create(World world, ChunkPos pos)
    {
        return factory.create(world, pos);
    }

    public boolean form(World world, BlockPos pos)
    {
        return form(world, pos, null);
    }

    public boolean form(World world, BlockPos pos, @Nullable Direction side)
    {
        return former != null && former.form(world, pos, side);
    }

    public boolean is(@Nullable MultiBlock multiBlock)
    {
        return multiBlock != null && multiBlock.type() == this;
    }

    @FunctionalInterface
    public interface MultiSupplier<T extends MultiBlock>
    {
        T create(World world, ChunkPos pos);
    }
}
