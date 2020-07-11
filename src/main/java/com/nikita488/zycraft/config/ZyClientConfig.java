package com.nikita488.zycraft.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ZyClientConfig
{
    public final ForgeConfigSpec.IntValue cloudTextureSize;

    public ZyClientConfig(ForgeConfigSpec.Builder builder)
    {
        cloudTextureSize = builder
                .comment("Defines the resolution of the Cloud texture (must be a power of two)")
                .defineInRange("cloudTextureSize", 32, 1, Integer.MAX_VALUE);
    }
}
