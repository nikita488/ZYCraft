package nikita488.zycraft.init;

import com.tterrag.registrate.Registrate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import nikita488.zycraft.ZYCraft;

public class ZYLang
{
    private static final Registrate REGISTRATE = ZYCraft.registrate();

    public static final MutableComponent SHIFT = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "shift", "Shift")
            .withStyle(ChatFormatting.GREEN);
    public static final MutableComponent RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "right_click", "Right click")
            .withStyle(ChatFormatting.GREEN);
    public static final MutableComponent SHIFT_RIGHT_CLICK = REGISTRATE.addLang("tooltip", ZYCraft.id("shortcut"), "shift_right_click", "Shift + Right click")
            .withStyle(ChatFormatting.GREEN);

    public static final MutableComponent TOOLTIP_HINT = addLangWithArgs("tooltip", ZYCraft.id("hint"), "Press %s for info", SHIFT)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);

    public static final MutableComponent CREATIVE_ONLY = REGISTRATE.addLang("tooltip", ZYCraft.id("creative_only"), "Creative-only")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);

    public static final MutableComponent COLORABLE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "Colorable")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);
    public static final MutableComponent COLORABLE_RED = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "red", "Red")
            .withStyle(ChatFormatting.RED);
    public static final MutableComponent COLORABLE_GREEN = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "green", "Green")
            .withStyle(ChatFormatting.GREEN);
    public static final MutableComponent COLORABLE_BLUE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "blue", "Blue Zychorium")
            .withStyle(ChatFormatting.BLUE);
    public static final MutableComponent COLORABLE_DARK = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "dark", "Dark Zychorium")
            .withStyle(ChatFormatting.DARK_GRAY);
    public static final MutableComponent COLORABLE_LIGHT = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "light", "Light Zychorium")
            .withStyle(ChatFormatting.WHITE);
    public static final MutableComponent COLORABLE_ANY_DYE = REGISTRATE.addLang("tooltip", ZYCraft.id("colorable"), "any_dye", "Any Dye")
            .withStyle(ChatFormatting.YELLOW);
    public static final MutableComponent COLORABLE_INFO = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "info", "%s or %s on this block with one of the following:", RIGHT_CLICK, SHIFT_RIGHT_CLICK)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLORABLE_RGB = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "rgb", "%s, %s or %s - to adjust each individual RGB", COLORABLE_RED, COLORABLE_GREEN, COLORABLE_BLUE)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLORABLE_BRIGHTNESS = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "brightness", "%s - to adjust color brightness", COLORABLE_DARK)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLORABLE_RESET = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "reset", "%s - to reset color", COLORABLE_LIGHT)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLORABLE_DYE = addLangWithArgs("tooltip", ZYCraft.id("colorable"), "dye", "%s - to apply color of the dye", COLORABLE_ANY_DYE)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);

    public static final MutableComponent COLOR_SCANNER_APPLY = addLangWithArgs("tooltip", ZYCraft.id("color_scanner"), "apply", "%s on %s block to apply color", RIGHT_CLICK, COLORABLE)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLOR_SCANNER_COPY = addLangWithArgs("tooltip", ZYCraft.id("color_scanner"), "copy", "%s on %s block to copy color", SHIFT_RIGHT_CLICK, COLORABLE)
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent COLOR_SCANNER_CURRENT_COLOR = REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "current_color", "Current color:")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final TranslatableComponent COLOR_SCANNER_RED = (TranslatableComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "red", "Red:    %s")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.RED);
    public static final TranslatableComponent COLOR_SCANNER_GREEN = (TranslatableComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "green", "Green: %s")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GREEN);
    public static final TranslatableComponent COLOR_SCANNER_BLUE = (TranslatableComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("color_scanner"), "blue", "Blue:   %s")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE);

    public static final TranslatableComponent SCYTHE_DURABILITY = REGISTRATE.addLang("tooltip", ZYCraft.id("scythe"), "durability", "Durability: %s%%");

    public static final MutableComponent INTERFACE = REGISTRATE.addLang("tooltip", ZYCraft.id("interface"), "Interface")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW);
    public static final MutableComponent VALVE_INFO = REGISTRATE.addLang("tooltip", ZYCraft.id("valve"), "info", "Allows for fluids to pass through")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent VALVE_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.id("valve"), "feature", "Fluids balance between valves")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent ITEM_IO_INFO = REGISTRATE.addLang("tooltip", ZYCraft.id("item_io"), "info", "Allows for items to pass through")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
    public static final MutableComponent ITEM_IO_FEATURE = REGISTRATE.addLang("tooltip", ZYCraft.id("item_io"), "feature", "Increases control over flow of items")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);

    public static final TranslatableComponent FLUID_TANK_FILLED = (TranslatableComponent)REGISTRATE.addLang("tooltip", ZYCraft.id("fluid_tank"), "filled", "%s/%s mB")
            .withStyle(ChatFormatting.GRAY);

    public static final TranslatableComponent FABRICATOR = REGISTRATE.addLang("container", ZYCraft.id("fabricator"), "Fabricator");
    public static final TranslatableComponent TANK = REGISTRATE.addLang("container", ZYCraft.id("tank"), "Multi-Tank");

    public static final TranslatableComponent NARRATE_GAUGE = REGISTRATE.addLang("gui", ZYCraft.id("narrate"), "gauge", "%s gauge: filled %s of %s");

    public static final TranslatableComponent FABRICATOR_AUTO_LOW = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "auto_low", "Auto: Low");
    public static final TranslatableComponent FABRICATOR_AUTO_HIGH = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "auto_high", "Auto: High");
    public static final TranslatableComponent FABRICATOR_PULSE = REGISTRATE.addLang("mode", ZYCraft.id("fabricator"), "pulse", "Pulse");
    public static final TranslatableComponent VALVE_IN = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("valve"), "in", "Input")
            .withStyle(ChatFormatting.BLUE);
    public static final TranslatableComponent VALVE_OUT = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("valve"), "out", "Output")
            .withStyle(ChatFormatting.GOLD);
    public static final TranslatableComponent ITEM_IO_ANY = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "any", "Input/Output")
            .withStyle(ChatFormatting.WHITE);
    public static final TranslatableComponent ITEM_IO_ALL_IN = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "all_in", "Input")
            .withStyle(ChatFormatting.BLUE);
    public static final TranslatableComponent ITEM_IO_ALL_OUT = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "all_out", "Output")
            .withStyle(ChatFormatting.GOLD);
    public static final TranslatableComponent ITEM_IO_IN1 = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "in1", "Input1")
            .withStyle(ChatFormatting.DARK_PURPLE);
    public static final TranslatableComponent ITEM_IO_OUT1 = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "out1", "Output1")
            .withStyle(ChatFormatting.DARK_RED);
    public static final TranslatableComponent ITEM_IO_IN2 = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "in2", "Input2")
            .withStyle(ChatFormatting.GREEN);
    public static final TranslatableComponent ITEM_IO_OUT2 = (TranslatableComponent)REGISTRATE.addLang("mode", ZYCraft.id("item_io"), "out2", "Output2")
            .withStyle(ChatFormatting.YELLOW);

    public static final TranslatableComponent CURRENT_COLOR_LABEL = (TranslatableComponent)REGISTRATE.addLang("label", ZYCraft.id("current_color"), "Current color:")
            .withStyle(ChatFormatting.GRAY);
    public static final TranslatableComponent MODE_LABEL = REGISTRATE.addLang("label", ZYCraft.id("mode"), "Mode: ");
    public static final TranslatableComponent SOURCE_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("source_fluid"), "Source fluid: ");
    public static final TranslatableComponent VOID_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("void_fluid"), "Void fluid: ");
    public static final TranslatableComponent STORED_FLUID_LABEL = REGISTRATE.addLang("label", ZYCraft.id("stored_fluid"), "Stored fluid: ");

    public static final TranslatableComponent RED_INFO = (TranslatableComponent)REGISTRATE.addLang("info", ZYCraft.id("red"), "Red: %s")
            .withStyle(ChatFormatting.RED);
    public static final TranslatableComponent GREEN_INFO = (TranslatableComponent)REGISTRATE.addLang("info", ZYCraft.id("green"), "Green: %s")
            .withStyle(ChatFormatting.GREEN);
    public static final TranslatableComponent BLUE_INFO = (TranslatableComponent)REGISTRATE.addLang("info", ZYCraft.id("blue"), "Blue: %s")
            .withStyle(ChatFormatting.BLUE);
    public static final TranslatableComponent FLUID_INFO = REGISTRATE.addLang("info", ZYCraft.id("fluid"), "%s %s mB");

    public static final TranslatableComponent FABRICATOR_RECIPE_INCOMPATIBLE = REGISTRATE.addLang("jei", ZYCraft.id("fabricator"), "recipe_incompatible", "Recipe incompatible with Fabricator");

    public static void init()
    {
        REGISTRATE.addLang("itemGroup", ZYCraft.id("fluids"), "ZYCraft Fluids");
        REGISTRATE.addLang("death.attack", ZYCraft.id("quartz_crystal"), "%1$s was slowly poked by Quartz Crystal");
        REGISTRATE.addLang("death.attack", ZYCraft.id("quartz_crystal"), "player", "%1$s was slowly poked by Quartz Crystal because of %2$s");
    }

    public static TranslatableComponent addLangWithArgs(String type, ResourceLocation id, String localizedName, Object... args)
    {
        return new TranslatableComponent(REGISTRATE.addLang(type, id, localizedName).getKey(), args);
    }

    public static TranslatableComponent addLangWithArgs(String type, ResourceLocation id, String suffix, String localizedName, Object... args)
    {
        return new TranslatableComponent(REGISTRATE.addLang(type, id, suffix, localizedName).getKey(), args);
    }

    public static MutableComponent copy(TranslatableComponent component, Object... args)
    {
        return new TranslatableComponent(component.getKey(), args).setStyle(component.getStyle());
    }
}
