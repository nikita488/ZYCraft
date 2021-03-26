package nikita488.zycraft.enums;

import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.Tags;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYItems;

public enum ViewerType implements IStringSerializable
{
    BASIC("glass", () -> DataIngredient.tag(Tags.Items.INGOTS_IRON), properties -> properties.strength(0.3F, 6.0F)),
    DIRE("dire", () -> DataIngredient.items(ZYBlocks.QUARTZ_CRYSTAL)),
    ALUMINIUM("aluminium", () -> DataIngredient.items(ZYItems.ALUMINIUM)),
    REINFORCED("reinforced", () -> DataIngredient.tag(Tags.Items.OBSIDIAN), properties -> properties.strength(0.3F, 1200.0F)),
    GLOWING("glowing", () -> DataIngredient.tag(Tags.Items.DUSTS_GLOWSTONE), properties -> properties.lightLevel(state -> 15)),
    DARK("dark", () -> DataIngredient.items(Items.INK_SAC));//TODO: Create tag with INK_SAC and DYES_BLACK included and use here

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

    @Override
    public String getSerializedName()
    {
        return name;
    }

    public DataIngredient ingredient()
    {
        return ingredient.get();
    }

    public NonNullUnaryOperator<Block.Properties> properties()
    {
        return properties;
    }
}
