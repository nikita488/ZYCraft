package nikita488.zycraft.init;

import nikita488.zycraft.ZYCraft;
import net.minecraft.util.DamageSource;

public class ModDamageSources
{
    public static final DamageSource QUARTZ_CRYSTAL = new DamageSource("quartz_crystal");

    public static void init()
    {
        addLang(QUARTZ_CRYSTAL, "%1$s death by Quartz Crystal", "%1$s walked into a Quartz Crystal because of %2$s");
    }

    private static void addLang(DamageSource source, String message, String messageCausedByPlayer)
    {
        ZYCraft.REGISTRY.addLang("death.attack", ZYCraft.modLoc(source.getDamageType()), message);
        ZYCraft.REGISTRY.addLang("death.attack", ZYCraft.modLoc(source.getDamageType()), "player", messageCausedByPlayer);
    }
}
