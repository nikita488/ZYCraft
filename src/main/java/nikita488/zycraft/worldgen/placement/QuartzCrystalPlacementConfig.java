package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class QuartzCrystalPlacementConfig implements IPlacementConfig
{
    public static final Codec<QuartzCrystalPlacementConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(Codec.INT.fieldOf("generation_attempts").orElse(0).forGetter(config -> config.generationAttempts),
                    Codec.INT.fieldOf("count").orElse(0).forGetter(config -> config.count))
            .apply(instance, QuartzCrystalPlacementConfig::new));

    public final int generationAttempts;
    public final int count;

    public QuartzCrystalPlacementConfig(int generationAttempts, int count)
    {
        this.generationAttempts = generationAttempts;
        this.count = count;
    }
}
