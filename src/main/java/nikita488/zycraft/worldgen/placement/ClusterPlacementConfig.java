package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ClusterPlacementConfig implements IPlacementConfig
{
    public static final Codec<ClusterPlacementConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("generation_attempts").orElse(0).forGetter(config -> config.generationAttempts),
                    Codec.INT.fieldOf("count").orElse(0).forGetter(config -> config.count))
                    .apply(instance, ClusterPlacementConfig::new));

    public final int generationAttempts;
    public final int count;

    public ClusterPlacementConfig(int generationAttempts, int count)
    {
        this.generationAttempts = generationAttempts;
        this.count = count;
    }
}
