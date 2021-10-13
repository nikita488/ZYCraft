package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import nikita488.zycraft.ZYCraft;

public class ZYLang
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final IFormattableTextComponent SHIFT = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "shift", "Shift")
            .withStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "right_click", "Right click")
            .withStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent SHIFT_RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "shift_right_click", "Shift + Right click")
            .withStyle(TextFormatting.GREEN);

    public static final IFormattableTextComponent TOOLTIP_HINT = addLangWithArgs("tooltip", ZYCraft.id("hint"), "Press %s for info", SHIFT)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);

    public static final IFormattableTextComponent CREATIVE_ONLY = REGISTRATE.addLang("tooltip", ZYCraft.id("creative_only"), "Creative-only")
            .withStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);

    public static final IFormattableTextComponent COLORABLE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "Colorable")
            .withStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);
    public static final IFormattableTextComponent COLORABLE_RED = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "red", "Red")
            .withStyle(TextFormatting.RED);
    public static final IFormattableTextComponent COLORABLE_GREEN = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "green", "Green")
            .withStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent COLORABLE_BLUE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "blue", "Blue Zychorium")
            .withStyle(TextFormatting.BLUE);
    public static final IFormattableTextComponent COLORABLE_DARK = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "dark", "Dark Zychorium")
            .withStyle(TextFormatting.DARK_GRAY);
    public static final IFormattableTextComponent COLORABLE_LIGHT = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "light", "Light Zychorium")
            .withStyle(TextFormatting.WHITE);
    public static final IFormattableTextComponent COLORABLE_ANY_DYE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "any_dye", "Any Dye")
            .withStyle(TextFormatting.YELLOW);
    public static final IFormattableTextComponent COLORABLE_INFO = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "info", "%s or %s on this block with one of the following:", RIGHT_CLICK, SHIFT_RIGHT_CLICK)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_RGB = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "rgb", "%s, %s or %s - to adjust each individual RGB", COLORABLE_RED, COLORABLE_GREEN, COLORABLE_BLUE)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_BRIGHTNESS = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "brightness", "%s - to adjust color brightness", COLORABLE_DARK)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_RESET = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "reset", "%s - to reset color", COLORABLE_LIGHT)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_DYE = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "dye", "%s - to apply color of the dye", COLORABLE_ANY_DYE)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);

    public static final IFormattableTextComponent COLOR_SCANNER_APPLY = addLangWithArgs("tooltip", ZYCraft.id("color_scanner"), "apply", "%s on %s block to apply color", RIGHT_CLICK, COLORABLE)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLOR_SCANNER_COPY = addLangWithArgs("tooltip", ZYCraft.id("color_scanner"), "copy", "%s on %s block to copy color", SHIFT_RIGHT_CLICK, COLORABLE)
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLOR_SCANNER_CURRENT_COLOR = REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "current_color", "Current color:")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final TranslationTextComponent COLOR_SCANNER_RED = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "red", "Red:    %d")
            .withStyle(TextFormatting.ITALIC, TextFormatting.RED);
    public static final TranslationTextComponent COLOR_SCANNER_GREEN = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "green", "Green: %d")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GREEN);
    public static final TranslationTextComponent COLOR_SCANNER_BLUE = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "blue", "Blue:   %d")
            .withStyle(TextFormatting.ITALIC, TextFormatting.BLUE);

    public static final TranslationTextComponent SCYTHE_DURABILITY = REGISTRATE.addLang("tooltip", ZYCraft.id("scythe"), "durability", "Durability: %.2f%%");

    public static final IFormattableTextComponent INTERFACE = REGISTRATE.addLang("tooltip", ZYCraft.id("interface"), "Interface")
            .withStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);
    public static final IFormattableTextComponent VALVE_INFO = REGISTRATE.addLang("tooltip", ZYCraft.id("valve"), "info", "Allows for fluids to pass through")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent VALVE_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.id("valve"), "feature", "Fluids balance between valves")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent ITEM_IO_INFO = REGISTRATE.addLang("tooltip", ZYCraft.id("item_io"), "info", "Allows for items to pass through")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent ITEM_IO_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.id("item_io"), "feature", "Increases control over flow of items")
            .withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);

    public static final TranslationTextComponent FLUID_TANK_FILLED = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("fluid_tank"), "filled", "%d/%d mB")
            .withStyle(TextFormatting.GRAY);

    public static final TranslationTextComponent FABRICATOR = REGISTRATE.addLang("container", ZYCraft.id("fabricator"), "Fabricator");
    public static final TranslationTextComponent TANK = REGISTRATE.addLang("container", ZYCraft.id("tank"), "Multi-Tank");

    public static final TranslationTextComponent NARRATE_GAUGE = REGISTRATE.addLang("gui", ZYCraft.id("narrate"), "gauge", "%s gauge: filled %d of %d");

    public static final TranslationTextComponent FABRICATOR_AUTO_LOW = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "auto_low", "Auto: Low");
    public static final TranslationTextComponent FABRICATOR_AUTO_HIGH = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "auto_high", "Auto: High");
    public static final TranslationTextComponent FABRICATOR_PULSE = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "pulse", "Pulse");
    public static final TranslationTextComponent VALVE_IN = REGISTRATE.addLang("mode", ZYCraft.id("valve"), "in", "Input");
    public static final TranslationTextComponent VALVE_OUT = REGISTRATE.addLang("mode", ZYCraft.id("valve"), "out", "Output");
    public static final TranslationTextComponent ITEM_IO_ANY = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "any", "Input/Output");
    public static final TranslationTextComponent ITEM_IO_ALL_IN = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "all_in", "Input");
    public static final TranslationTextComponent ITEM_IO_ALL_OUT = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "all_out", "Output");
    public static final TranslationTextComponent ITEM_IO_IN1 = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "in1", "Input1");
    public static final TranslationTextComponent ITEM_IO_OUT1 = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "out1", "Output1");
    public static final TranslationTextComponent ITEM_IO_IN2 = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "in2", "Input2");
    public static final TranslationTextComponent ITEM_IO_OUT2 = REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "out2", "Output2");

    public static final TranslationTextComponent MODE_LABEL = REGISTRATE.addLang("label", ZYCraft.id("mode"), "Mode: ");
    public static final TranslationTextComponent SOURCE_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("source_fluid"), "Source fluid: ");
    public static final TranslationTextComponent VOID_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("void_fluid"), "Void fluid: ");
    public static final TranslationTextComponent STORED_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("stored_fluid"), "Stored fluid: ");

    public static final TranslationTextComponent FLUID_INFO = REGISTRATE.addLang("info", ZYCraft.id("fluid"), "%s %d mB");

    public static final TranslationTextComponent FABRICATOR_RECIPE_INCOMPATIBLE = REGISTRATE.addLang("jei", ZYCraft.id("fabricator"), "recipe_incompatible", "Recipe incompatible with Fabricator");

    public static void init()
    {
        REGISTRATE.addLang("itemGroup", ZYCraft.id("fluids"), "ZYCraft Fluids");
        REGISTRATE.addLang("death.attack", ZYCraft.id("quartz_crystal"), "%1$s was slowly poked by Quartz Crystal");
        REGISTRATE.addLang("death.attack", ZYCraft.id("quartz_crystal"), "player", "%1$s was slowly poked by Quartz Crystal because of %2$s");
    }

    public static TranslationTextComponent addLangWithArgs(String type, ResourceLocation id, String localizedName, Object... args)
    {
        return new TranslationTextComponent(REGISTRATE.addLang(type, id, localizedName).getKey(), args);
    }

    public static TranslationTextComponent addLangWithArgs(String type, ResourceLocation id, String suffix, String localizedName, Object... args)
    {
        return new TranslationTextComponent(REGISTRATE.addLang(type, id, suffix, localizedName).getKey(), args);
    }

    public static IFormattableTextComponent copy(TranslationTextComponent component, Object... args)
    {
        return new TranslationTextComponent(component.getKey(), args).setStyle(component.getStyle());
    }
}
