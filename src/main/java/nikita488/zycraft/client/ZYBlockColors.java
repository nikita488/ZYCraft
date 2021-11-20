package nikita488.zycraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nikita488.zycraft.api.colorable.IColorable;
import nikita488.zycraft.block.entity.FabricatorBlockEntity;
import nikita488.zycraft.block.entity.FluidSelectorBlockEntity;
import nikita488.zycraft.enums.ZYType;
import nikita488.zycraft.multiblock.child.block.ItemIOBlock;
import nikita488.zycraft.multiblock.child.block.ValveBlock;
import nikita488.zycraft.multiblock.child.block.entity.ConvertedMultiChildBlockEntity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public enum ZYBlockColors implements Supplier<BlockColor>
{
    COLORABLE((state, getter, pos, tintIndex) -> getBlockEntity(state, getter, pos) instanceof IColorable colorable ? colorable.getColor(state, getter, pos, tintIndex) : 0xFFFFFF),
    FABRICATOR((state, getter, pos, tintIndex) -> getBlockEntity(state, getter, pos) instanceof FabricatorBlockEntity fabricator ? fabricator.getColor(state) : ZYType.BLUE.rgb()),
    CONVERTED_MULTI_CHILD((state, getter, pos, tintIndex) -> getBlockEntity(state, getter, pos) instanceof ConvertedMultiChildBlockEntity converted ? Minecraft.getInstance().getBlockColors().getColor(converted.initialState(), getter, pos, tintIndex) : 0xFFFFFF),
    VALVE((state, getter, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ValveBlock.IO_MODE).rgb() : ZYType.BLUE.rgb()),
    ITEM_IO((state, getter, pos, tintIndex) -> tintIndex == 1 ? state.getValue(ItemIOBlock.IO_MODE).rgb() : ZYType.GREEN.rgb()),
    FLUID_SELECTOR((state, getter, pos, tintIndex) -> getBlockEntity(state, getter, pos) instanceof FluidSelectorBlockEntity selector ? selector.getColor() : 0xFFFFFF);

    public static final Map<ZYType, BlockColor> ZY_COLORS = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, BlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, getter, pos, tintIndex) -> type.rgb());

        return builder.build();
    });
    public static final Map<ZYType, BlockColor> ZY_COLORS_WITH_OVERLAY = Util.make(() ->
    {
        ImmutableMap.Builder<ZYType, BlockColor> builder = ImmutableMap.builder();

        for (ZYType type : ZYType.VALUES)
            builder.put(type, (state, getter, pos, tintIndex) -> tintIndex == 1 ? type.overlayRGB() : type.rgb());

        return builder.build();
    });
    private final BlockColor color;

    ZYBlockColors(BlockColor color)
    {
        this.color = color;
    }

    public static BlockColor getZYBlockColor(ZYType type)
    {
        return getZYBlockColor(type, false);
    }

    public static BlockColor getZYBlockColor(ZYType type, boolean coloredOverlay)
    {
        return (coloredOverlay ? ZY_COLORS_WITH_OVERLAY : ZY_COLORS).get(type);
    }

    @Nullable
    private static BlockEntity getBlockEntity(BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos)
    {
        return getter != null && pos != null && state.hasBlockEntity() ? getter.getBlockEntity(pos) : null;
    }

    @Override
    public BlockColor get()
    {
        return color;
    }
}
