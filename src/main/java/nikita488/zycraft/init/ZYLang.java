package nikita488.zycraft.init;

import net.minecraft.util.text.TranslationTextComponent;
import nikita488.zycraft.ZYCraft;

public class ZYLang
{
    public static final TranslationTextComponent TOOLTIP_HINT =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("hint"), "\u00A77Press \u00A7a\u00A7oShift\u00A77 for info");

    public static final TranslationTextComponent COLORABLE =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "\u00A7e\u00A7oColorable");
    public static final TranslationTextComponent COLORABLE_INFO =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "info", "\u00A7a\u00A7oRight click\u00A77\u00A7o or \u00A7a\u00A7oShift + Right click\u00A77\u00A7o on this block with one of the following:");
    public static final TranslationTextComponent COLORABLE_RGB =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "rgb", "\u00A7c\u00A7oRed\u00A77\u00A7o, \u00A7a\u00A7oGreen\u00A77\u00A7o or \u00A79\u00A7oBlue Zychorium\u00A77\u00A7o - to adjust each individual RGB");
    public static final TranslationTextComponent COLORABLE_BRIGHTNESS =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "brightness", "\u00A78\u00A7oDark Zychorium\u00A77\u00A7o - to adjust color brightness");
    public static final TranslationTextComponent COLORABLE_RESET =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "reset", "\u00A7f\u00A7oLight Zychorium\u00A77\u00A7o - to reset color");
    public static final TranslationTextComponent COLORABLE_DYE =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("colorable"), "dye", "\u00A7e\u00A7oAny Dye\u00A77\u00A7o - to apply color of the dye");

    public static final TranslationTextComponent COLOR_SCANNER_APPLY =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "apply", "\u00A7a\u00A7oRight click\u00A77\u00A7o on \u00A7e\u00A7oColorable \u00A77\u00A7oblock to apply color");
    public static final TranslationTextComponent COLOR_SCANNER_COPY =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "copy", "\u00A7a\u00A7oShift + Right click\u00A77\u00A7o on \u00A7e\u00A7oColorable \u00A77\u00A7oblock to copy color");
    public static final TranslationTextComponent COLOR_SCANNER_CURRENT_COLOR =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "current_color", "\u00A77Current color:");
    public static final TranslationTextComponent COLOR_SCANNER_RED =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "red", "\u00A7c\u00A7oRed:    %s");
    public static final TranslationTextComponent COLOR_SCANNER_GREEN =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "green", "\u00A7a\u00A7oGreen: %s");
    public static final TranslationTextComponent COLOR_SCANNER_BLUE =
            ZYCraft.REGISTRY.addLang("tooltip", ZYCraft.modLoc("color_scanner"), "blue", "\u00A79\u00A7oBlue:   %s");

    public static final TranslationTextComponent QUARTZ_CRYSTAL =
            ZYCraft.REGISTRY.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "%1$s was slowly poked by Quartz Crystal");
    public static final TranslationTextComponent QUARTZ_CRYSTAL_PLAYER =
            ZYCraft.REGISTRY.addLang("death.attack", ZYCraft.modLoc("quartz_crystal"), "player", "%1$s was slowly poked by Quartz Crystal because of %2$s");

    public static void init() {}

    public static TranslationTextComponent getColorScannerRed(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_RED.getKey(), amount);
    }

    public static TranslationTextComponent getColorScannerGreen(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_GREEN.getKey(), amount);
    }

    public static TranslationTextComponent getColorScannerBlue(int amount)
    {
        return new TranslationTextComponent(COLOR_SCANNER_BLUE.getKey(), amount);
    }
}
