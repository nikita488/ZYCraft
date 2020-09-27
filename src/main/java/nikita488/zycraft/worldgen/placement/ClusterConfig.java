package nikita488.zycraft.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class ClusterConfig implements IPlacementConfig
{
    public static final Codec<ClusterConfig> CODEC = RecordCodecBuilder.create((instance) ->
    {
        return instance.group(Codec.INT.fieldOf("generation_attempts").orElse(0).forGetter((config) ->
        {
            return config.generationAttempts;
        }), Codec.INT.fieldOf("count").orElse(0).forGetter((config) ->
        {
            return config.count;
        })).apply(instance, ClusterConfig::new);
    });
    public final int generationAttempts;
    public final int count;

    public ClusterConfig(int generationAttempts, int count)
    {
        this.generationAttempts = generationAttempts;
        this.count = count;
    }
}
