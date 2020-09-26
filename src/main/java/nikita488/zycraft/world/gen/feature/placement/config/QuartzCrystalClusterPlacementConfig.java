package nikita488.zycraft.world.gen.feature.placement.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class QuartzCrystalClusterPlacementConfig implements IPlacementConfig
{
    public final int generationAttempts;
    public final int count;

    public QuartzCrystalClusterPlacementConfig(int generationAttempts, int count)
    {
        this.generationAttempts = generationAttempts;
        this.count = count;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                ops.createString("generation_attempts"), ops.createInt(this.generationAttempts),
                ops.createString("count"), ops.createInt(this.count))));
    }

    public static QuartzCrystalClusterPlacementConfig deserialize(Dynamic<?> dynamic)
    {
        int generationAttempts = dynamic.get("generation_attempts").asInt(0);
        int count = dynamic.get("count").asInt(0);
        return new QuartzCrystalClusterPlacementConfig(generationAttempts, count);
    }
}
