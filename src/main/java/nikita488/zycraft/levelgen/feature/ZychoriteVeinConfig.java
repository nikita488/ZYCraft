package nikita488.zycraft.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class ZychoriteVeinConfig implements FeatureConfiguration
{
    public static final Codec<ZychoriteVeinConfig> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RuleTest.CODEC.fieldOf("target").forGetter(config -> config.target),
                    Codec.intRange(0, 64).fieldOf("size").forGetter(config -> config.size),
                    Codec.floatRange(0F, 1F).fieldOf("ore_percentage").forGetter(config -> config.orePercentage))
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
