package nikita488.zycraft.util;

import net.minecraft.util.DamageSource;
import nikita488.zycraft.ZYCraft;

public class ZYDamageSource extends DamageSource
{
    public ZYDamageSource(String damageType)
    {
        super(ZYCraft.MOD_ID + "." + damageType);
    }
}
