package nikita488.zycraft.multiblock;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.init.ZYRegistries;
import nikita488.zycraft.multiblock.former.IMultiFormer;

import javax.annotation.Nullable;
import java.util.Optional;

public class MultiType<T extends MultiBlock>
{
    private final MultiSupplier<? extends T> factory;
    private final IMultiFormer former;

    public MultiType(MultiSupplier<? extends T> factory, IMultiFormer former)
    {
        this.factory = factory;
        this.former = former;
    }

    public static Optional<MultiType<?>> load(CompoundTag tag)
    {
        return Optional.ofNullable(ZYRegistries.MULTI_TYPES.get().getValue(new ResourceLocation(tag.getString("ID"))));
    }

    public static Optional<MultiBlock> create(Level level, ChunkPos mainChunk, CompoundTag tag)
    {
        try
        {
            return createUnchecked(level, mainChunk, tag);
        }
        catch (Throwable throwable)
        {
            ZYCraft.LOGGER.error("Exception loading MultiBlock {}", tag.getString("ID"), throwable);
            return Optional.empty();
        }
    }

    private static Optional<MultiBlock> createUnchecked(Level level, ChunkPos mainChunk, CompoundTag tag)
    {
        return Util.ifElse(load(tag).map(type -> type.create(level, mainChunk)), multiBlock ->
        {
            multiBlock.load(tag);
            multiBlock.initChildBlocks();
        }, () -> ZYCraft.LOGGER.warn("Skipping MultiBlock {}", tag.getString("ID")));
    }

    public T create(Level level, ChunkPos pos)
    {
        return factory.create(level, pos);
    }

    public boolean form(BlockState interfaceState, Level level, BlockPos interfacePos)
    {
        return form(interfaceState, level, interfacePos, null);
    }

    public boolean form(BlockState interfaceState, Level level, BlockPos interfacePos, @Nullable Direction formingSide)
    {
        return former.form(interfaceState, level, interfacePos, formingSide);
    }

    public boolean is(MultiBlock multiBlock)
    {
        return multiBlock != null && multiBlock.type() == this;
    }

    public static boolean tryFormMultiBlock(BlockState interfaceState, Level level, BlockPos interfacePos, @Nullable Direction formingSide)
    {
        if (!level.isClientSide())
            for (MultiType<?> type : ZYRegistries.MULTI_TYPES.get().getValues())
                if (type.form(interfaceState, level, interfacePos, formingSide))
                    return true;
        return false;
    }

    @FunctionalInterface
    public interface MultiSupplier<T extends MultiBlock>
    {
        T create(Level level, ChunkPos pos);
    }
}
