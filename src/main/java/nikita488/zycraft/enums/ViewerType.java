package nikita488.zycraft.enums;

import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import nikita488.zycraft.init.ZYBlocks;
import nikita488.zycraft.init.ZYItems;
import nikita488.zycraft.init.ZYTags;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.Tag;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.Tags;

public enum ViewerType implements IStringSerializable
{
    GLASS("glass", () -> DataIngredient.tag(Tags.Items.INGOTS_IRON), properties -> properties.strength(0.3F, 6.0F)),
    DIRE("dire", () -> DataIngredient.items(ZYBlocks.QUARTZ_CRYSTAL)),
    ALUMINIUM("aluminium", () -> DataIngredient.items(ZYItems.ALUMINIUM)),
    REINFORCED("reinforced", () -> DataIngredient.tag(Tags.Items.OBSIDIAN), properties -> properties.strength(0.3F, 1200.0F)),
    GLOWING("glowing", () -> DataIngredient.tag(Tags.Items.DUSTS_GLOWSTONE), properties -> properties.lightLevel(state -> 15)),
    DARK("dark", () -> DataIngredient.items(Items.INK_SAC)),
    IMMORTAL("immortal", () -> DataIngredient.tag(Tags.Items.GLASS)),
    GLOWING_IMMORTAL("glowing_immortal", () -> DataIngredient.tag(Tags.Items.DUSTS_GLOWSTONE), properties -> properties.lightLevel(state -> 15)),
    DARK_IMMORTAL("dark_immortal", () -> DataIngredient.tag(Tags.Items.DYES_BLACK));

    private final String name;
    private final NonNullSupplier<DataIngredient> ingredient;
    private final NonNullUnaryOperator<Block.Properties> propertiesCallback;
    public static final ViewerType[] VALUES = new ViewerType[] {GLASS, DIRE, ALUMINIUM, REINFORCED, GLOWING, DARK};
    public static final ViewerType[] IMMORTAL_VALUES = new ViewerType[] {IMMORTAL, GLOWING_IMMORTAL, DARK_IMMORTAL};

    ViewerType(String name, NonNullSupplier<DataIngredient> ingredient)
    {
        this(name, ingredient, NonNullUnaryOperator.identity());
    }

    ViewerType(String name, NonNullSupplier<DataIngredient> ingredient, NonNullUnaryOperator<Block.Properties> propertiesCallback)
    {
        this.name = name;
        this.ingredient = ingredient;
        this.propertiesCallback = propertiesCallback;
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

    public Block.Properties properties(Block.Properties initialProperties, boolean phantomized)
    {
        return propertiesCallback.andThen(p -> phantomized ? p.noCollission() : p).apply(initialProperties);
    }

    public boolean isImmortal()
    {
        return this == IMMORTAL || this == GLOWING_IMMORTAL || this == DARK_IMMORTAL;
    }

    public RenderType layer()
    {
        if (isImmortal())
            return RenderType.translucent();

        return this == DARK ? RenderType.translucent() : RenderType.cutout();
    }

    public ITag.INamedTag<Block> tag(boolean phantomized)
    {
        if (isImmortal())
            return phantomized ? ZYTags.Blocks.VIEWERS_PHANTOMIZED_IMMORTAL : ZYTags.Blocks.VIEWERS_IMMORTAL;

        return phantomized ? ZYTags.Blocks.VIEWERS_PHANTOMIZED : ZYTags.Blocks.VIEWERS_BASE;
    }


}
