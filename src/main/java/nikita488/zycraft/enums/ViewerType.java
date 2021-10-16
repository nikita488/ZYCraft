package nikita488.zycraft.enums;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYItems;

import java.util.Map;

public enum ViewerType
{
    BASIC("glass", () -> DataIngredient.tag(Tags.Items.INGOTS_IRON), properties -> properties.strength(0.3F, 6F)),
    DIRE("dire", () -> DataIngredient.items(ZYBlocks.QUARTZ_CRYSTAL)),
    ALUMINIUM("aluminium", () -> DataIngredient.items(ZYItems.ALUMINIUM)),
    REINFORCED("reinforced", () -> DataIngredient.tag(Tags.Items.OBSIDIAN), properties -> properties.strength(0.3F, 1200F)),
    GLOWING("glowing", () -> DataIngredient.tag(Tags.Items.DUSTS_GLOWSTONE), properties -> properties.lightLevel(state -> 15)),
    DARK("dark", () -> DataIngredient.items(Items.INK_SAC));

    private final String name;
    private final NonNullSupplier<DataIngredient> ingredient;
    private final NonNullUnaryOperator<Block.Properties> properties;
    public static final ViewerType[] VALUES = new ViewerType[] {BASIC, DIRE, ALUMINIUM, REINFORCED, GLOWING, DARK};
    public static final ViewerType[] IMMORTAL_VALUES = new ViewerType[] {BASIC, GLOWING, DARK};

    ViewerType(String name, NonNullSupplier<DataIngredient> ingredient)
    {
        this(name, ingredient, NonNullUnaryOperator.identity());
    }

    ViewerType(String name, NonNullSupplier<DataIngredient> ingredient, NonNullUnaryOperator<Block.Properties> properties)
    {
        this.name = name;
        this.ingredient = ingredient;
        this.properties = properties;
    }

    public static <T> Map<ViewerType, T> buildMap(String pattern, boolean immortal, NonNullBiFunction<ViewerType, String, T> factory)
    {
        ImmutableMap.Builder<ViewerType, T> map = ImmutableMap.builder();
        ViewerType[] values = immortal ? IMMORTAL_VALUES : VALUES;

        for (ViewerType type : values)
        {
            String target = immortal && type == BASIC ? "{type}_" : "{type}";
            String replacement = immortal && type == BASIC ? "" : type.toString();
            map.put(type, factory.apply(type, pattern.replace(target, replacement)));
        }

        return map.build();
    }

    public DataIngredient ingredient()
    {
        return ingredient.get();
    }

    public NonNullUnaryOperator<Block.Properties> properties()
    {
        return properties;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
