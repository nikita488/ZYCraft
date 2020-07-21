package nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZYClientConfig
{
    protected final ForgeConfigSpec.IntValue animationSize;

    public ZYClientConfig(ForgeConfigSpec.Builder builder)
    {
        animationSize = builder
                .comment("Defines the resolution of the animation texture used by this mod (must be a power of two). Reload resource packs to apply new resolution (F3 + T).")
                .defineInRange("animationSize", 32, 1, Integer.MAX_VALUE);
    }
}
