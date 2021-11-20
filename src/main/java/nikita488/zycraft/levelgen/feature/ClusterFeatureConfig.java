package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class ClusterFeatureConfig implements FeatureConfiguration
{
    public static final Codec<ClusterFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RuleTest.CODEC.fieldOf("target").forGetter(config -> config.target),
                    Codec.intRange(1, 5).fieldOf("size").forGetter(config -> config.size))
            .apply(instance, ClusterFeatureConfig::new));

    public final RuleTest target;
    public final int size;

    public ClusterFeatureConfig(RuleTest target, int size)
    {
        this.target = target;
        this.size = size;
    }
}
