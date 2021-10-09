package nikita488.zycraft.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.former.IMultiFormer;

import javax.annotation.Nullable;
import java.util.Optional;

public class MultiType<T extends MultiBlock> extends ForgeRegistryEntry<MultiType<?>>
{
    private final MultiSupplier<? extends T> factory;
    private final IMultiFormer former;

    public MultiType(MultiSupplier<? extends T> factory, IMultiFormer former)
    {
        this.factory = factory;
        this.former = former;
    }

    public static Optional<MultiType<?>> load(CompoundNBT tag)
    {
        return Optional.ofNullable(ZYRegistries.MULTI_TYPES.get().getValue(new ResourceLocation(tag.getString("ID"))));
    }

    public static Optional<MultiBlock> create(World world, ChunkPos mainChunk, CompoundNBT tag)
    {
        try
        {
            return createUnchecked(world, mainChunk, tag);
        }
        catch (Throwable throwable)
        {
            ZYCraft.LOGGER.error("Exception loading MultiBlock {}", tag.getString("ID"), throwable);
            return Optional.empty();
        }
    }

    private static Optional<MultiBlock> createUnchecked(World world, ChunkPos mainChunk, CompoundNBT tag)
    {
        return Util.acceptOrElse(load(tag).map(type -> type.create(world, mainChunk)), multiBlock ->
        {
            multiBlock.load(tag);
            multiBlock.initChildBlocks();
        }, () -> ZYCraft.LOGGER.warn("Skipping MultiBlock {}", tag.getString("ID")));
    }

    public T create(World world, ChunkPos pos)
    {
        return factory.create(world, pos);
    }

    public boolean form(BlockState interfaceState, World world, BlockPos interfacePos)
    {
        return form(interfaceState, world, interfacePos, null);
    }

    public boolean form(BlockState interfaceState, World world, BlockPos interfacePos, @Nullable Direction formingSide)
    {
        return former.form(interfaceState, world, interfacePos, formingSide);
    }

    public boolean is(MultiBlock multiBlock)
    {
        return multiBlock != null && multiBlock.type() == this;
    }

    public static boolean tryFormMultiBlock(BlockState interfaceState, World world, BlockPos interfacePos, @Nullable Direction formingSide)
    {
        if (!world.isRemote())
            for (MultiType<?> type : ZYRegistries.MULTI_TYPES.get().getValues())
                if (type.form(interfaceState, world, interfacePos, formingSide))
                    return true;
        return false;
    }

    @FunctionalInterface
    public interface MultiSupplier<T extends MultiBlock>
    {
        T create(World world, ChunkPos pos);
    }
}
