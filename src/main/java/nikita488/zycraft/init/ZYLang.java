package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import nikita488.zycraft.ZYCraft;

public class ZYLang
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final TranslationTextComponent TOOLTIP_HINT =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("hint"), "\u00A77Press \u00A7a\u00A7oShift\u00A77 for info");

    public static final TranslationTextComponent COLORABLE =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "\u00A7e\u00A7oColorable");
    public static final TranslationTextComponent COLORABLE_INFO =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "info", "\u00A7a\u00A7oRight click\u00A77\u00A7o or \u00A7a\u00A7oShift + Right click\u00A77\u00A7o on this block with one of the following:");
    public static final TranslationTextComponent COLORABLE_RGB =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "rgb", "\u00A7c\u00A7oRed\u00A77\u00A7o, \u00A7a\u00A7oGreen\u00A77\u00A7o or \u00A79\u00A7oBlue Zychorium\u00A77\u00A7o - to adjust each individual RGB");
    public static final TranslationTextComponent COLORABLE_BRIGHTNESS =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "brightness", "\u00A78\u00A7oDark Zychorium\u00A77\u00A7o - to adjust color brightness");
    public static final TranslationTextComponent COLORABLE_RESET =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "reset", "\u00A7f\u00A7oLight Zychorium\u00A77\u00A7o - to reset color");
    public static final TranslationTextComponent COLORABLE_DYE =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("colorable"), "dye", "\u00A7e\u00A7oAny Dye\u00A77\u00A7o - to apply color of the dye");

    public static final TranslationTextComponent COLOR_SCANNER_APPLY =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "apply", "\u00A7a\u00A7oRight click\u00A77\u00A7o on \u00A7e\u00A7oColorable \u00A77\u00A7oblock to apply color");
    public static final TranslationTextComponent COLOR_SCANNER_COPY =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "copy", "\u00A7a\u00A7oShift + Right click\u00A77\u00A7o on \u00A7e\u00A7oColorable \u00A77\u00A7oblock to copy color");
    public static final TranslationTextComponent COLOR_SCANNER_CURRENT_COLOR =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "current_color", "\u00A77Current color:");
    public static final TranslationTextComponent COLOR_SCANNER_RED =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "red", "Red:    %s");
    public static final TranslationTextComponent COLOR_SCANNER_GREEN =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "green", "Green: %s");
    public static final TranslationTextComponent COLOR_SCANNER_BLUE =
            REGISTRATE.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "blue", "Blue:   %s");

    public static final TranslationTextComponent QUARTZ_CRYSTAL =
            REGISTRATE.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "%1$s was slowly poked by Quartz Crystal");
    public static final TranslationTextComponent QUARTZ_CRYSTAL_PLAYER =
            REGISTRATE.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "player", "%1$s was slowly poked by Quartz Crystal because of %2$s");

    public static final TranslationTextComponent FLUIDS_ITEM_GROUP =
            REGISTRATE.addLang("itemGroup", ZYCraft.modLoc("fluids"), "ZYCraft Fluids");

    public static final TranslationTextComponent FABRICATOR =
            REGISTRATE.addLang("container", ZYCraft.modLoc("fabricator"), "Fabricator");

    public static void init() {}

    public static IFormattableTextComponent getColorScannerRed(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_RED.getKey(), amount).mergeStyle(TextFormatting.ITALIC, TextFormatting.RED);
    }

    public static IFormattableTextComponent getColorScannerGreen(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_GREEN.getKey(), amount).mergeStyle(TextFormatting.ITALIC, TextFormatting.GREEN);
    }

    public static IFormattableTextComponent getColorScannerBlue(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_BLUE.getKey(), amount).mergeStyle(TextFormatting.ITALIC, TextFormatting.BLUE);
    }
}
