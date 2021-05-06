package nikita488.zycraft.client.resources;

import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;

import java.util.Locale;

public enum ZYWidget
{
    BACKGROUND,
    ARROW;

    private final ResourceLocation name;
    public static final ZYWidget[] VALUES = values();

    ZYWidget()
    {
        this.name = ZYCraft.modLoc(name().toLowerCase(Locale.ROOT));
    }

    public ResourceLocation textureName()
    {
        return name;
    }
}
