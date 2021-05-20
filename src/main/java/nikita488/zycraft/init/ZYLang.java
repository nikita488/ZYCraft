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

    public static final IFormattableTextComponent SHIFT = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("shortcut"), "shift", "Shift")
            .mergeStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("shortcut"), "right_click", "Right click")
            .mergeStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent SHIFT_RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("shortcut"), "shift_right_click", "Shift + Right click")
            .mergeStyle(TextFormatting.GREEN);

    public static final IFormattableTextComponent TOOLTIP_HINT = addLangWithArgs("tooltip", ZYCraft.modLoc("hint"), "Press %s for info", SHIFT)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);

    public static final IFormattableTextComponent COLORABLE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "Colorable")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);
    public static final IFormattableTextComponent COLORABLE_RED = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "red", "Red")
            .mergeStyle(TextFormatting.RED);
    public static final IFormattableTextComponent COLORABLE_GREEN = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "green", "Green")
            .mergeStyle(TextFormatting.GREEN);
    public static final IFormattableTextComponent COLORABLE_BLUE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "blue", "Blue Zychorium")
            .mergeStyle(TextFormatting.BLUE);
    public static final IFormattableTextComponent COLORABLE_DARK = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "dark", "Dark Zychorium")
            .mergeStyle(TextFormatting.DARK_GRAY);
    public static final IFormattableTextComponent COLORABLE_LIGHT = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "light", "Light Zychorium")
            .mergeStyle(TextFormatting.WHITE);
    public static final IFormattableTextComponent COLORABLE_ANY_DYE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "any_dye", "Any Dye")
            .mergeStyle(TextFormatting.YELLOW);
    public static final IFormattableTextComponent COLORABLE_INFO = addLangWithArgs("tooltip", ZYCraft.modLoc("colorable"), "info", "%s or %s on this block with one of the following:", RIGHT_CLICK, SHIFT_RIGHT_CLICK)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_RGB = addLangWithArgs("tooltip", ZYCraft.modLoc("colorable"), "rgb", "%s, %s or %s - to adjust each individual RGB", COLORABLE_RED, COLORABLE_GREEN, COLORABLE_BLUE)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_BRIGHTNESS = addLangWithArgs("tooltip", ZYCraft.modLoc("colorable"), "brightness", "%s - to adjust color brightness", COLORABLE_DARK)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_RESET = addLangWithArgs("tooltip", ZYCraft.modLoc("colorable"), "reset", "%s - to reset color", COLORABLE_LIGHT)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLORABLE_DYE = addLangWithArgs("tooltip", ZYCraft.modLoc("colorable"), "dye", "%s - to apply color of the dye", COLORABLE_ANY_DYE)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);

    public static final IFormattableTextComponent COLOR_SCANNER_APPLY = addLangWithArgs("tooltip", ZYCraft.modLoc("color_scanner"), "apply", "%s on %s block to apply color", RIGHT_CLICK, COLORABLE)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLOR_SCANNER_COPY = addLangWithArgs("tooltip", ZYCraft.modLoc("color_scanner"), "copy", "%s on %s block to copy color", SHIFT_RIGHT_CLICK, COLORABLE)
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent COLOR_SCANNER_CURRENT_COLOR = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "current_color", "Current color:")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    private static final TranslationTextComponent COLOR_SCANNER_RED = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "red", "Red:    %d")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.RED);
    private static final TranslationTextComponent COLOR_SCANNER_GREEN = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "green", "Green: %d")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GREEN);
    private static final TranslationTextComponent COLOR_SCANNER_BLUE = (TranslationTextComponent)REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "blue", "Blue:   %d")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.BLUE);

    public static final TranslationTextComponent FABRICATOR = REGISTRATE.addLang("container", ZYCraft.modLoc("fabricator"), "Fabricator");
    public static final TranslationTextComponent AUTO_LOW = REGISTRATE.addLang("mode", ZYCraft.modLoc("fabricator.auto_low"), "Auto: Low");
    public static final TranslationTextComponent AUTO_HIGH = REGISTRATE.addLang("mode", ZYCraft.modLoc("fabricator.auto_high"), "Auto: High");
    public static final TranslationTextComponent PULSE = REGISTRATE.addLang("mode", ZYCraft.modLoc("fabricator.pulse"), "Pulse");
    public static final TranslationTextComponent RECIPE_INCOMPATIBLE = REGISTRATE.addLang("jei", ZYCraft.modLoc("fabricator.recipe.incompatible"), "Recipe incompatible with Fabricator");

    public static final IFormattableTextComponent INTERFACE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("interface"), "Interface")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.YELLOW);
    public static final IFormattableTextComponent VALVE_INFO = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("valve"), "info", "Allows for fluids to pass through")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent VALVE_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("valve"), "feature", "Fluids balance between valves")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent ITEM_IO_INFO = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("item_io"), "info", "Allows for items to pass through")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    public static final IFormattableTextComponent ITEM_IO_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.modLoc("item_io"), "feature", "Increases control over flow of items")
            .mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY);


    public static void init()
    {
        REGISTRATE.addLang("itemGroup", ZYCraft.modLoc("fluids"), "ZYCraft Fluids");
        REGISTRATE.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "%1$s was slowly poked by Quartz Crystal");
        REGISTRATE.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "player", "%1$s was slowly poked by Quartz Crystal because of %2$s");
    }

    public static TranslationTextComponent addLangWithArgs(String type, ResourceLocation id, String localizedName, Object... args)
    {
        return new TranslationTextComponent(REGISTRATE.addLang(type, id, localizedName).getKey(), args);
    }

    public static TranslationTextComponent addLangWithArgs(String type, ResourceLocation id, String suffix, String localizedName, Object... args)
    {
        return new TranslationTextComponent(REGISTRATE.addLang(type, id, suffix, localizedName).getKey(), args);
    }

    public static IFormattableTextComponent getColorScannerRed(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_RED.getKey(), amount).setStyle(COLOR_SCANNER_RED.getStyle());
    }

    public static IFormattableTextComponent getColorScannerGreen(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_GREEN.getKey(), amount).setStyle(COLOR_SCANNER_GREEN.getStyle());
    }

    public static IFormattableTextComponent getColorScannerBlue(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_BLUE.getKey(), amount).setStyle(COLOR_SCANNER_BLUE.getStyle());
    }
}
