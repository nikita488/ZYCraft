package nikita488.zycraft.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;

public class QuartzCrystalFeatureConfig implements IFeatureConfig
{
    public static final Codec<QuartzCrystalFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RuleTest.CODEC.fieldOf("target").forGetter(config -> config.target),
                    Codec.intRange(1, 5).fieldOf("size").forGetter(config -> config.size))
            .apply(instance, QuartzCrystalFeatureConfig::new));

    public final RuleTest target;
    public final int size;

    public QuartzCrystalFeatureConfig(RuleTest target, int size)
    {
        this.target = target;
        this.size = size;
    }
}
