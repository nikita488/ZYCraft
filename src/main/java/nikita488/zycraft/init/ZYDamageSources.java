package nikita488.zycraft.init;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import nikita488.zycraft.ZYCraft;
import nikita488.zycraft.util.ZYDamageSource;

public class ZYDamageSources
{
    public static final ZYDamageSource QUARTZ_CRYSTAL = new ZYDamageSource("quartz_crystal");

    public static void init()
    {
        addLang(QUARTZ_CRYSTAL, "%1$s was slowly poked by Quartz Crystal", "%1$s was slowly poked by Quartz Crystal because of %2$s");
    }

    private static void addLang(DamageSource source, String message, String messageCausedByPlayer)
    {
        ZYCraft.REGISTRY.addRawLang("death.attack." + source.getDamageType(), message);
        ZYCraft.REGISTRY.addRawLang("death.attack." + source.getDamageType() + ".player", messageCausedByPlayer);
    }
}
