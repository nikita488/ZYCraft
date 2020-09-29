package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class ZychoriteVeinConfig implements IFeatureConfig
{
    public static final Codec<ZychoriteVeinConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RuleTest.field_237127_c_.fieldOf("target").forGetter(config -> config.target),
                    Codec.intRange(0, 64).fieldOf("size").forGetter(config -> config.size),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("ore_percentage").forGetter(config -> config.orePercentage))
                    .apply(instance, ZychoriteVeinConfig::new));

    public final RuleTest target;
    public final int size;
    public final float orePercentage;

    public ZychoriteVeinConfig(RuleTest target, int size, float orePercentage)
    {
        this.target = target;
        this.size = size;
        this.orePercentage = orePercentage;
    }
}
